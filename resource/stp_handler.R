# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ #
# ProjectName:  stp
# Purpose:      sales training
# programmer:   Anqi Chen
# Date:         20-11-2017
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ #


##-- handle the exception thrown out
exception_handle <- tryCatch({
  # library(shiny)
  # library(shinydashboard)
  # library(shinyjs)
  # library(DT)
  library(plyr)
  library(dplyr)
  library(tidyr)
  library(jsonlite)
  library(digest)
  library(openxlsx)
  library(mongolite)
  options(scipen=200,
          mongodb = list(
            "host" = "127.0.0.1:27017"
            # "username" = "root",
            # "password" = "root"
          ))
  
  
  argss <- commandArgs(TRUE)
  R_File_Path <- argss[1]
  R_Json_Path <- argss[2]
  load(R_File_Path)
  print(R_File_Path)
  para <- fromJSON(R_Json_Path, flatten = TRUE)

  
  ## arg in need 
  phase <- para$phase
  user_name <- para$user_name
  input <- para
  
  
  
  
  if (phase ==1) {
    inter_data <- tmp0
    last_report1_1 <- p0_report
    last_acc_success_value <- 0
  } else {
    
    
    ## mongoDB
    db <- mongo(collection = user_name,
                url = sprintf(
                  "mongodb://%s/%s",
                  # options()$mongodb$username,
                  # options()$mongodb$password,
                  options()$mongodb$host,
                  "STP"))
    
    # Read all the entries
    transfer <- db$find()
    
    inter_data <- transfer$p1_tmp[[1]]
    last_report1_1 <- transfer$p1_report[[1]]
    last_acc_success_value <- transfer$p1_acc_success_value[[1]]
  }
  
  
  ##~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  ##                              curve function
  ##~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  
  curve <- function(curve.no,input_x){
    data <- curve.no
    if (input_x<=min(data$x)) {
      y <- data$y[which.min(data$x)]
    } else if (input_x>=max(data$x)) {
      y <- data$y[which.max(data$x)]
    } else {
      left <- data[which.min(abs(input_x-data$x)),]
      tmp <- data[-which.min(abs(input_x-data$x)),]
      right <- tmp[which.min(abs(input_x-tmp$x)),]
      y <- ifelse(left$x <= right$x,
                  (1-(input_x-left$x)/(right$x-left$x))*left$y + 
                    (1-(right$x-input_x)/(right$x-left$x))*right$y,
                  (1-(input_x-right$x)/(left$x-right$x))*right$y + 
                    (1-(left$x-input_x)/(left$x-right$x))*left$y)}
    
    y
  }
  
  ##~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  ##                      data cleaning part
  ##~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  
  get.data1 <- function(input,phase){
    data_decision <- data.frame(
      phase = NULL, 
      hospital = NULL,
      sales_rep = NULL,
      product = NULL,
      sales_target = NULL,
      potential_revenue = NULL,
      promotional_budget = NULL,
      sr_time_proportion = NULL,
      stringsAsFactors = F)
    
    for (j in 1:10) {
      for (q in 1:4){
        name.phase = as.character(paste("周期",phase,sep=""))
        name.hospital = as.character(unique(hospital_info$名称)[j])
        name.product = as.character(product_info$类别[q])
        name.sales_rep <- as.character(input[[paste("p",phase,"_sr_hosp",j,sep="")]])
        value.sales_target <- as.numeric(input[[paste("p",phase,"_hosp",j,"_sales_target_",q,sep="")]])
        value.promotional_budget <- as.numeric(input[[paste("p",phase,"_promotional_budget_hosp",j,sep="")]])/100*
          total_promotional_budget[which(total_promotional_budget$phase==paste("周期",phase,sep="")),]$budget
        value.sr_time_proportion <- as.numeric(input[[paste("p",phase,"_hosp",j,"_worktime_",q,sep="")]])/100
        
        
        data_decision <- plyr::rbind.fill(data_decision,data.frame(
          phase = name.phase,
          hospital = name.hospital,
          sales_rep = ifelse(name.sales_rep=="",0,name.sales_rep), 
          product = name.product,
          sales_target = ifelse(is.na(value.sales_target),0,value.sales_target),
          potential_revenue = as.numeric(hospital_info[which(hospital_info$phase==name.phase&
                                                               hospital_info$名称==name.hospital&
                                                               hospital_info$产品==name.product),]$`潜力(元)`),
          promotional_budget = ifelse(is.na(value.promotional_budget),0,value.promotional_budget),
          sr_time_proportion = ifelse(is.null(name.sales_rep)|is.na(value.sr_time_proportion),
                                      0,value.sr_time_proportion)
        ))
      }}
    data_decision
  }
  
  get.data2 <- function(input,phase){
    data_decision2 <- data.frame(
      phase = NULL,
      sales_rep = NULL,
      sales_training = NULL,
      product_training = NULL,
      field_work = NULL,
      meetings_with_team = NULL,
      kpi_analysis = NULL,
      admin_work = NULL,
      work_time = NULL,
      stringsAsFactors = F
    )
    
    for (j in 1:5) {
      name.sales_rep <- as.character(sr_info_list$sales_rep[j])
      value.sales_training <- as.numeric(
        input[[paste("p",phase,"_sr",j,"_sales_training",sep="")]])
      value.product_training <- as.numeric(
        input[[paste("p",phase,"_sr",j,"_product_training",sep="")]])
      value.field_work <- as.numeric(
        input[[paste("p",phase,"_sr",j,"_field_work",sep="")]])
      value.meetings_with_team <- as.numeric(
        input[[paste("p",phase,"_flm_team_meeting",sep="")]])
      value.kpi_analysis <- as.numeric(
        input[[paste("p",phase,"_flm_kpi_analysis",sep="")]])
      value.admin_work <- as.numeric(
        input[[paste("p",phase,"_flm_admin_work",sep="")]])
      
      if (name.sales_rep==""){
        data_decision2 <- plyr::rbind.fill(data_decision2,data.frame(
          phase = as.character(paste("周期",phase,sep="")),
          sales_rep = 0,
          sales_training = 0,
          product_training = 0,
          field_work = 0,
          meetings_with_team = 0,
          kpi_analysis = 0,
          admin_work = 0,
          work_time = 0))
      } else{
        
        data_decision2 <- plyr::rbind.fill(data_decision2,data.frame(
          phase = as.character(paste("周期",phase,sep="")),
          sales_rep = name.sales_rep,
          sales_training = ifelse(is.na(value.sales_training),0,value.sales_training),
          product_training = ifelse(is.na(value.product_training),0,value.product_training),
          field_work = ifelse(is.na(value.field_work),0,value.field_work),
          meetings_with_team = ifelse(is.na(value.meetings_with_team),0,value.meetings_with_team),
          kpi_analysis = ifelse(is.na(value.kpi_analysis),0,value.kpi_analysis),
          admin_work = ifelse(is.na(value.admin_work),0,value.admin_work),
          work_time = worktime
        ))}
    }
    data_decision2
  }
  
  sales_training <- function(input,phase){sum(c(
    as.numeric(input[[paste("p",phase,"_sr1_sales_training",sep = "")]]),
    as.numeric(input[[paste("p",phase,"_sr2_sales_training",sep = "")]]),
    as.numeric(input[[paste("p",phase,"_sr3_sales_training",sep = "")]]),
    as.numeric(input[[paste("p",phase,"_sr4_sales_training",sep = "")]]),
    as.numeric(input[[paste("p",phase,"_sr5_sales_training",sep = "")]])),
    na.rm = T)}
  
  field_work <- function(input,phase){sum(c(
    as.numeric(input[[paste("p",phase,"_sr1_field_work",sep = "")]]),
    as.numeric(input[[paste("p",phase,"_sr2_field_work",sep = "")]]),
    as.numeric(input[[paste("p",phase,"_sr3_field_work",sep = "")]]),
    as.numeric(input[[paste("p",phase,"_sr4_field_work",sep = "")]]),
    as.numeric(input[[paste("p",phase,"_sr5_field_work",sep = "")]])),
    na.rm = T)}
  

  
  
  
  get.data3 <- function(input,phase){
    flm_decision <- data.frame(
      flm_sales_training = sales_training(input,phase),
      flm_field_work = field_work(input,phase),
      flm_meetings_with_team = ifelse(is.na(as.numeric(input[[paste("p",phase,"_flm_team_meeting",sep = "")]])),
                                      0,
                                      as.numeric(input[[paste("p",phase,"_flm_team_meeting",sep = "")]])),
      flm_kpi_analysis = ifelse(is.na(as.numeric(input[[paste("p",phase,"_flm_kpi_analysis",sep = "")]])),
                                0,
                                as.numeric(input[[paste("p",phase,"_flm_kpi_analysis",sep = "")]])),
      flm_admin_work = ifelse(is.na(as.numeric(input[[paste("p",phase,"_flm_admin_work",sep = "")]])),
                              0,
                              as.numeric(input[[paste("p",phase,"_flm_admin_work",sep = "")]])),
      stringsAsFactors = F)
    flm_decision
    
  }
  
  cp_data1 <- get.data1(input,phase)
  cp_data2 <- get.data2(input,phase)
  flm_data <- get.data3(input,phase)
  pp_data1 <- inter_data %>% select(hospital,
                                hosp_code,
                                product,
                                prod_code,
                                real_revenue,
                                real_volume,
                                sr_sales_performance,
                                deployment_quality_index,
                                customer_relationship_index,
                                promotional_support_index,
                                sales_performance,
                                offer_attractiveness,
                                acc_offer_attractiveness) %>%
    mutate(acc_success_value = last_acc_success_value)%>%
    distinct()
  
  colnames(pp_data1)[5:14] <- paste("pp_",colnames(pp_data1)[5:14],sep="")
  
  pp_data2 <- inter_data %>% select(sales_rep,
                                sales_level,
                                real_revenue_by_sr,
                                real_volume_by_sr,
                                sr_acc_revenue,
                                sales_skills_index,
                                product_knowledge_index,
                                motivation_index,
                                sr_acc_field_work,
                                target_revenue_realization_by_sr) %>%
    distinct()
  
  colnames(pp_data2)[3:10] <- paste("pp_",colnames(pp_data2)[3:10],sep="")
  
  
  
  ##----------------------------------------------------------------------------
  ##--                 data processing
  ##----------------------------------------------------------------------------
  calculation <- function(pp_data1,
                          pp_data2,
                          cp_data1,
                          cp_data2){
    #
    #
    tmp1 <- left_join(cp_data1,pp_data1,by=c("hospital","product"))
    tmp2 <- left_join(cp_data2,pp_data2,by=c("sales_rep"))
    
    tmp <- left_join(tmp1,tmp2,by=c("phase","sales_rep")) %>%
      dplyr::mutate(sales_rep = ifelse(sr_time_proportion==0,"0",sales_rep),
                    sales_level = ifelse(sr_time_proportion==0,"0",sales_level),
                    pp_real_volume_by_sr = ifelse(sr_time_proportion==0,0,pp_real_volume_by_sr),
                    pp_real_revenue_by_sr = ifelse(sr_time_proportion==0,0,pp_real_revenue_by_sr),
                    pp_sr_acc_revenue = ifelse(sr_time_proportion==0,0,pp_sr_acc_revenue),
                    pp_sales_skills_index = ifelse(sr_time_proportion==0,0,pp_sales_skills_index),
                    pp_product_knowledge_index = ifelse(sr_time_proportion==0,0,pp_product_knowledge_index),
                    pp_motivation_index = ifelse(sr_time_proportion==0,0,pp_motivation_index),
                    pp_sr_acc_field_work = ifelse(sr_time_proportion==0,0,pp_sr_acc_field_work),
                    pp_target_revenue_realization_by_sr = ifelse(sr_time_proportion==0,0,pp_target_revenue_realization_by_sr),
                    sales_training = ifelse(sr_time_proportion==0,0,sales_training),
                    product_training = ifelse(sr_time_proportion==0,0,product_training),
                    field_work = ifelse(sr_time_proportion==0,0,field_work),
                    meetings_with_team = ifelse(sr_time_proportion==0,0,meetings_with_team),
                    kpi_analysis = ifelse(sr_time_proportion==0,0,kpi_analysis),
                    admin_work = ifelse(sr_time_proportion==0,0,admin_work),
                    work_time = ifelse(sr_time_proportion==0,0,work_time)) %>%
      dplyr::mutate(product_price = sapply(product,function(x) product_info[which(product_info$类别==x),]$`单价（公司考核价）`),
                    target_revenue= sales_target,
                    target_volume = round(target_revenue/product_price)) %>%
      group_by(phase,sales_rep) %>%
      dplyr::mutate(other_time=work_time-(
        product_training+
          meetings_with_team),
        sr_time=sr_time_proportion*other_time,
        no.hospitals = n_distinct(hospital),
        sr_time_total=sum(sr_time,na.rm=T),
        last_revenue_by_sr = sum(pp_real_revenue,na.rm=T)) %>%
      ungroup %>%
      group_by(phase,hospital) %>%
      dplyr::mutate(sr_time_by_hosp=sum(sr_time,na.rm=T)) %>%
      ungroup() %>%
      dplyr::mutate(product_time_proportion=round(sr_time/ifelse(sr_time_by_hosp==0,0.0001,sr_time_by_hosp),2),
                    promotional_budget = round(promotional_budget*product_time_proportion),
                    promotional_factor = ifelse(target_revenue==0,0,round(promotional_budget/target_revenue*100,2)),
                    sr_acc_field_work = pp_sr_acc_field_work+field_work,
                    overhead_factor = sapply(pp_motivation_index,function(x) curve(curve12,x)),
                    overhead_time = round(overhead_factor*overhead,0),
                    real_sr_time = round(sr_time-overhead_time*sr_time_proportion,2),
                    pp_experience_index = round(sapply(pp_sr_acc_revenue,function(x) round(curve(curve11,x),2))),
                    field_work_peraccount = field_work/ifelse(no.hospitals==0,0.0001,no.hospitals),
                    product_knowledge_addition_current_period = sapply(product_training,function(x)curve(curve26,x)),
                    product_knowledge_transfer_value = sapply(pp_product_knowledge_index,function(x)curve(curve28,x)),
                    ss_accumulated_field_work_delta = sapply(sr_acc_field_work,function(x)curve(curve42,x)),
                    ss_accumulated_sales_training_delta = sapply(sales_training,function(x)curve(curve43,x)),
                    ss_experience_index_pp = sapply(pp_experience_index,function(x)curve(curve44,x)),
                    m_sales_training_delta = sapply(sales_training,function(x)curve(curve17,x)),
                    m_admin_work_delta = sapply(admin_work,function(x)curve(curve18,x)))%>%
      dplyr::mutate(sales_skills_index = round(
        (ss_accumulated_field_work_delta+pp_sales_skills_index)*((weightage$sales_skills)$field_work)+
          (ss_accumulated_sales_training_delta+pp_sales_skills_index)*((weightage$sales_skills)$sales_training)+
          (ss_experience_index_pp+pp_sales_skills_index)*((weightage$sales_skills)$experience)),
        product_knowledge_index = round(
          product_knowledge_addition_current_period+
            pp_product_knowledge_index)) %>%
      dplyr::mutate(srsp_motivation_delta = sapply(pp_motivation_index,function(x)curve(curve32,x)),
                    srsp_sales_skills_delta = sapply(sales_skills_index,function(x)curve(curve34,x)),
                    srsp_product_knowledge_delta = sapply(product_knowledge_index,
                                                          function(x)curve(curve33,x)),
                    srsp_time_with_account_delta =  mapply(function(x,y,z){ if(
                      x==as.character(product_info_list$product[2])){
                      curve(curve36,y)} else if (
                        x==as.character(product_info_list$product[3])) {
                        curve(curve37,y)} else if (x==as.character(product_info_list$product[4])) {
                          curve(curve38,y)} else if (x==as.character(product_info_list$product[1])&
                                                     z %in% c("人民医院","中日医院")){
                            curve(curve39,y)} else if (x==as.character(product_info_list$product[1])&
                                                       z =="大学医院"){
                              curve(curve39_1,y)}else{curve(curve35,y)}},
                      product,real_sr_time,hospital)) %>%
      dplyr::mutate(sr_sales_performance =
                      (srsp_motivation_delta+pp_sr_sales_performance)*
                      ((weightage$sr_sales_performance)$motivation)+
                      (srsp_sales_skills_delta+pp_sr_sales_performance)*
                      ((weightage$sr_sales_performance)$sales_skills)+
                      (srsp_product_knowledge_delta+pp_sr_sales_performance)*
                      ((weightage$sr_sales_performance)$product_knowledge)+
                      (srsp_time_with_account_delta+pp_sr_sales_performance)*
                      ((weightage$sr_sales_performance)$time_with_account))%>%
      dplyr::mutate(sr_sales_performance = ifelse(sr_sales_performance<0,0,sr_sales_performance),
                    dq_admin_work_delta = sapply(admin_work,function(x)curve(curve5,x)),
                    dq_meetings_with_team_delta =sapply(meetings_with_team,function(x)curve(curve7,x)),
                    dq_kpi_analysis_factor = sapply(kpi_analysis,function(x)curve(curve8,x)))%>%
      dplyr::mutate(deployment_quality_index = round(
        (pp_deployment_quality_index+dq_admin_work_delta)*
          ((weightage$deployment_quality)$admin_work)+
          (pp_deployment_quality_index+dq_meetings_with_team_delta)*
          ((weightage$deployment_quality)$meetings_with_team)+
          pp_deployment_quality_index*dq_kpi_analysis_factor*
          ((weightage$deployment_quality)$kpi_report_analysis)))%>%
      dplyr::mutate(deployment_quality_index = ifelse(deployment_quality_index<0,0,deployment_quality_index),
                    ps_promotional_budget_factor = sapply(promotional_factor,function(x)curve(curve30,x))) %>%
      dplyr::mutate(promotional_support_index = 
                      pp_promotional_support_index*ps_promotional_budget_factor) %>%
      dplyr::mutate(promotional_support_index = ifelse(promotional_support_index<0,0,promotional_support_index),
                    sp_field_work_delta = sapply(field_work_peraccount,function(x)curve(curve40,x)),
                    sp_deployment_quality_delta = sapply(deployment_quality_index,function(x)curve(curve41,x))) %>%
      dplyr::mutate(sales_performance = 
                      sr_sales_performance*((weightage$sales_performance)$sr_sales_performance)+
                      (pp_sales_performance+sp_field_work_delta)*
                      ((weightage$sales_performance)$field_work)+
                      (pp_sales_performance+sp_deployment_quality_delta)*
                      ((weightage$sales_performance)$deployment_quality))%>%
      dplyr::mutate(sales_performance = ifelse(sales_performance<0,0,sales_performance),
                    cr_product_knowledge_delta = 
                      sapply(product_knowledge_index,function(x)curve(curve2,x)),
                    cr_promotional_support_delta = 
                      sapply(ps_promotional_budget_factor,function(x)curve(curve3,x)),
                    cr_pp_customer_relationship_index = 
                      sapply(pp_customer_relationship_index,function(x)curve(curve4,x)))%>%
      dplyr::mutate(customer_relationship_index = 
                      (cr_pp_customer_relationship_index+cr_product_knowledge_delta)*
                      (weightage$customer_relaitonship)$product_knowledge+
                      (cr_pp_customer_relationship_index+cr_promotional_support_delta)*
                      (weightage$customer_relaitonship)$promotional_support) %>%
      dplyr::mutate(customer_relationship_index=ifelse(customer_relationship_index<0,0,customer_relationship_index))%>%
      dplyr::mutate(oa_customer_relationship_factor = 
                      mapply(function(x,y){if (x==as.character(product_info_list$product[1])){
                        curve(curve19,y)} else if(
                          x==as.character(product_info_list$product[2])){
                          curve(curve20,y)} else if (
                            x==as.character(product_info_list$product[3])) {
                            curve(curve21,y)} else {
                              curve(curve22,y)}},
                        product,customer_relationship_index),
                    oa_sales_performance_factor = sapply(sales_performance,function(x)curve(curve25,x))) %>%
      dplyr::mutate(cp_offer_attractiveness = 
                      oa_customer_relationship_factor*100*
                      (weightage$cp_offer_attractiveness)$customer_relationship+
                      oa_sales_performance_factor*100*
                      (weightage$cp_offer_attractiveness)$sales_performance) %>%
      dplyr::mutate(cp_offer_attractiveness = ifelse(sales_rep==0,0,cp_offer_attractiveness),
                    offer_attractiveness = 
                      cp_offer_attractiveness*(weightage$total_attractiveness)$cp_offer_attractiveness+
                      pp_offer_attractiveness*(weightage$total_attractiveness)$pp_offer_attractiveness,
                    acc_offer_attractiveness = round(pp_acc_offer_attractiveness+offer_attractiveness),
                    market_share =  mapply(function(x,y){if (x==as.character(product_info_list$product[1])){
                      curve(curve51_1,y)} else if(
                        x==as.character(product_info_list$product[2])){
                        curve(curve51_2,y)} else if (
                          x==as.character(product_info_list$product[3])) {
                          curve(curve51_3,y)} else {
                            curve(curve51_4,y)}},
                      product,offer_attractiveness),
                    real_revenue = round(market_share/100*potential_revenue),
                    real_volume = round(real_revenue/product_price)) %>%
      ungroup() %>%
      dplyr::group_by(phase,sales_rep) %>%
      dplyr::mutate(target_revenue_by_sr = sum(target_revenue,na.rm=T),
                    target_revenue_percent = target_revenue_by_sr/last_revenue_by_sr,
                    bonus_factor = sapply(target_revenue_percent,function(x) {if (is.nan(x)|x<0.5) {
                      0 } else if (x>=0.5 & x<1) {0.5} else {1}}),
                    real_revenue_by_sr = sum(real_revenue,na.rm=T),
                    target_revenue_realization_by_sr = round(real_revenue_by_sr/target_revenue_by_sr*100,2),
                    target_volume_by_sr = sum(target_volume,na.rm=T),
                    real_volume_by_sr = sum(real_volume,na.rm=T),
                    target_volume_realization_by_sr = round(real_volume_by_sr/target_volume_by_sr*100,2),
                    bonus_tmp = mapply(function(x,y) {if(is.nan(x)) {
                      0} else if (x >= 90 & x <= 120){
                        round(x/100*y*0.03)} else if(x >120) {
                          round(1.2*y*0.03)} else {0}},
                      target_revenue_realization_by_sr,real_revenue_by_sr),
                    bonus = round(bonus_tmp*bonus_factor),
                    sr_acc_revenue = real_revenue_by_sr+pp_sr_acc_revenue,
                    experience_index = round(sapply(sr_acc_revenue, function(x) round(curve(curve11,x),2))),
                    m_meeting_with_team_delta =  mapply(function(x,y){
                      if (x == "junior") {
                        curve(curve13,y)
                      } else if(x=="middle"){
                        curve(curve14,y)
                      } else if(x=="senior"){
                        curve(curve15,y)
                      } else{0}
                    },sales_level,
                    meetings_with_team,SIMPLIFY=T),
                    m_sales_target_realization_delta = sapply(target_revenue_realization_by_sr,function(x) 
                      if (!is.nan(x)) {curve(curve16,x)} else {0}),
                    motivation_index = round(
                      (pp_motivation_index+m_admin_work_delta)*
                        ((weightage$motivation)$admin_work)+
                        (pp_motivation_index+m_sales_target_realization_delta)*
                        ((weightage$motivation)$sales_target_realization)+
                        (pp_motivation_index+m_meeting_with_team_delta)*
                        ((weightage$motivation)$meetings_with_team)+
                        (pp_motivation_index+m_sales_training_delta)*
                        ((weightage$motivation)$sales_training))) %>%
      dplyr::mutate(motivation_index=ifelse(sales_rep==0,0,motivation_index)) %>%
      ungroup()
    
    
    tmp
  }
  
  
  data_to_use <- calculation(pp_data1,
                             pp_data2,
                             cp_data1,
                             cp_data2)
  
  
  ##----------------------------------------------------------------------------
  ##--                 making reports
  ##----------------------------------------------------------------------------
  
  report_data <- function(tmp,flm_data,null_report) {
    
    tmp1 <- tmp %>% 
      dplyr::mutate(product = factor(product,levels=c("口服抗生素",
                                                      "一代降糖药",
                                                      "三代降糖药",
                                                      "皮肤药"))) 
    tmp2 <- tmp %>%
      filter(sales_rep!="0") %>%
      dplyr::mutate(sales_rep=factor(sales_rep,levels = c("小宋",
                                                          "小兰",
                                                          "小木",
                                                          "小白",
                                                          "小青")))
    
    ## report 1——1
    profit_tmp <- tmp1 %>%
      select(real_revenue,
             real_volume,
             promotional_budget,
             product) %>%
      dplyr::mutate(production_cost = sapply(product,function(x)product_info[which(product_info$类别==x),]$单位成本),
                    production_fee = round(production_cost*real_volume),
                    total_revenue =round(sum(real_revenue,na.rm=T)),
                    total_production_fee =round(sum(production_fee,na.rm=T)),
                    total_promotional_budget = round(sum(promotional_budget,na.rm=T)),
                    total_profit = total_revenue-total_production_fee-total_promotional_budget)  %>%
      select(total_profit) %>%
      distinct()
    
    
    report1_mod1 <- tmp1 %>%
      select(phase,
             sales_rep,
             hospital,
             product_knowledge_index,
             sales_skills_index,
             customer_relationship_index,
             motivation_index,
             real_revenue,
             pp_acc_success_value) %>%
      distinct() %>%
      dplyr::mutate(total_revenue = round(sum(real_revenue,na.rm=T),2)) %>%
      filter(sales_rep!="0") %>%
      dplyr::mutate(average_customer_relationship_index = round(mean(customer_relationship_index,na.rm=T),2),
                    average_sales_skills_index = round(mean(sales_skills_index,na.rm=T),2),
                    average_product_knowledge_index = round(mean(product_knowledge_index,na.rm=T),2),
                    average_motivation_index = round(mean(motivation_index,na.rm=T),2),
                    team_capability = round((average_motivation_index +
                                               average_product_knowledge_index +
                                               average_sales_skills_index)/3)) %>%
      select(phase,
             total_revenue,
             average_customer_relationship_index,
             team_capability,
             pp_acc_success_value) %>%
      distinct() %>%
      dplyr::mutate(profit=as.numeric(profit_tmp),
                    inter1=(weightage$success_value)$total_sales*curve(curve50,total_revenue),
                    inter2=(weightage$success_value)$team_capability*curve(curve46,team_capability),
                    inter3=(weightage$success_value)$contribution_margin*curve(curve49,profit),
                    success_value = round(inter1+inter2+inter3),
                    acc_success_value = success_value + pp_acc_success_value) %>%
      dplyr::mutate(success_value = ifelse(phase=="周期0","",success_value),
                    acc_success_value = ifelse(phase=="周期0","",acc_success_value)) %>%
      select(phase,
             total_revenue,
             profit,
             team_capability,
             success_value,
             acc_success_value) %>%
      distinct()
    
    acc_success_value <- ifelse(is.na(as.numeric(report1_mod1$acc_success_value)),0,
                                as.numeric(report1_mod1$acc_success_value))
    
    colnames(report1_mod1) <- c("phase",
                                "总销售(元)",
                                "总利润(元)",
                                "团队能力(指数)",
                                "得分",
                                "累计得分") 
    report1_mod1_tmp <- null_report
    
    report1_mod1_tmp[which(report1_mod1_tmp$phase==report1_mod1$phase),2:6] <- report1_mod1[1,2:6]
    

    
    ## report 1——2
    report1_mod2 <- tmp1 %>%
      select(hospital,
             hosp_code,
             product,
             real_revenue,
             pp_real_revenue) %>%
      group_by(hospital) %>%
      dplyr::mutate(hospital_revenue = round(sum(real_revenue,na.rm=T)),
                    pp_hospital_revenue = round(sum(pp_real_revenue,na.rm=T))) %>%
      ungroup() %>%
      select(hospital,
             hosp_code,
             pp_hospital_revenue,
             hospital_revenue) %>%
      distinct() %>%
      arrange(hosp_code) %>%
      select(-hosp_code)
    
    colnames(report1_mod2) <- c("医院",
                                "上期总销售(元)",
                                "当期总销售(元)")

    
    ## report 2——1
    report2_mod1 <- tmp2 %>%
      group_by(sales_rep) %>%
      dplyr::mutate(visit_time=round(sum(real_sr_time,na.rm=T)),
                    total_sr_time=round(overhead_time+
                                          product_training+
                                          meetings_with_team+
                                          visit_time)) %>%
      ungroup() %>%
      select(overhead_time,
             product_training,
             meetings_with_team,
             visit_time,
             total_sr_time,
             sales_rep) %>%
      distinct()
    
    colnames(report2_mod1) <- c("日常事物(天)",
                                "产品培训(天)",
                                "团队会议(天)",
                                "医院拜访(天)",
                                "总工作时间(天)",
                                "销售代表")
    
    report2_mod1 <- report2_mod1 %>%
      gather(variable,`值`,-`销售代表`) %>%
      spread(`销售代表`,`值`) 
    
    report2_rank1 <- data.frame(
      variable=c("日常事物(天)",
                 "产品培训(天)",
                 "团队会议(天)",
                 "医院拜访(天)",
                 "总工作时间(天)"),
      rank=1:5,
      stringsAsFactors = F
    )
    
    report2_mod1 <- report2_mod1 %>%
      left_join(report2_rank1,by="variable") %>%
      arrange(rank) %>%
      dplyr::mutate("名称"=variable) %>%
      select(-rank,-variable)
  
    
    
    
    ## report 2——2
    report2_mod2 <- tmp2 %>%
      select(sales_rep,
             pp_product_knowledge_index,
             product_knowledge_index) %>%
      distinct()
    
    colnames(report2_mod2) <- c("销售代表",
                                "前期产品知识(指数)",
                                "当期产品知识(指数)")
    
    report2_mod2 <- report2_mod2 %>%
      gather(variable,`值`,-`销售代表`) %>%
      spread(`销售代表`,`值`) %>%
      dplyr::mutate("名称"=variable) %>%
      select(-variable)
    
    
    ## report 2——3
    report2_mod3 <- tmp2 %>%
      select(pp_experience_index,
             experience_index,
             sales_rep) %>%
      distinct()
    
    colnames(report2_mod3) <- c("前期经验",
                                "当期经验",
                                "销售代表")
    
    report2_mod3 <- report2_mod3 %>%
      gather(variable,`值`,-`销售代表`) %>%
      spread(`销售代表`,`值`)  %>%
      dplyr::mutate("名称"=variable) %>%
      select(-variable)
    
   
    
    ## report 2——4
    report2_mod4 <- tmp2 %>%
      select(sales_rep,
             pp_sales_skills_index,
             sales_skills_index) %>%
      distinct()
    colnames(report2_mod4) <- c("销售代表",
                                "前期销售技巧(指数)",
                                "当期销售技巧(指数)")
    report2_mod4 <- report2_mod4 %>%
      gather(variable,`值`,-`销售代表`) %>%
      spread(`销售代表`,`值`) %>%
      dplyr::mutate("名称"=variable) %>%
      select(-variable)
    
    
    ## report 2——5
    report2_mod5 <- tmp2 %>%
      select(sales_rep,
             pp_motivation_index,
             motivation_index) %>%
      distinct()  
    colnames(report2_mod5) <- c("销售代表",
                                "前期工作积极性(指数)",
                                "当期工作积极性(指数)")
    report2_mod5 <- report2_mod5 %>%
      gather(variable,`值`,-`销售代表`) %>%
      spread(`销售代表`,`值`) %>%
      dplyr::mutate("名称"=variable) %>%
      select(-variable)
    
    
    ## report 3——1
    flm_report <- tmp1 %>%
      select(sales_rep,
             bonus) %>%
      distinct() %>%
      dplyr::mutate(all_sr_bonus=sum(bonus,na.rm=T)) %>%
      select(all_sr_bonus) %>%
      distinct()
    
    flm_report <- flm_data %>%
      dplyr::mutate(all_sr_bonus = flm_report$all_sr_bonus,
                    work_time=flm_sales_training+
                      flm_field_work+
                      flm_meetings_with_team+
                      flm_kpi_analysis+
                      flm_admin_work)
    
    report3_mod1 <- tmp1 %>%
      filter(sales_rep!=0) %>%
      select(sales_rep,bonus) %>%
      distinct() %>%
      dplyr::mutate(sales_rep=factor(sales_rep,levels = c("小宋",
                                                          "小兰",
                                                          "小白",
                                                          "小木",
                                                          "小青"))) %>%
      do(plyr::rbind.fill(.,data.frame(sales_rep="总体",
                                       bonus = sum(.$bonus)))) %>%
      arrange(sales_rep) 
    
    colnames(report3_mod1) <- c("销售代表","绩效奖金(元)")
    
    report3_mod2 <- flm_report %>%
      select(-all_sr_bonus) 
    
    colnames(report3_mod2) <- c("能力辅导(天)",
                                "经理随访(天)",
                                "团队会议(天)",
                                "KPI分析(天)",
                                "行政工作(天)",
                                "总工作时间(天)")
    report3_mod2 <- report3_mod2 %>%
      gather(variable,`值`)  %>%
      dplyr::mutate("名称"=variable) %>%
      select(-variable)
    
    
    
    
    ## report 4——1
    report4_rank1 <- data.frame(
      "因素"=c("销售代表",
             "计划时间分配(天)",
             "实际时间分配(天)",
             "销售额(元)",
             "推广费用(元)",
             "生产成本(元)",
             "利润(元)"),
      rank=1:7,
      stringsAsFactors =F
    )
    
    report4_mod1 <- tmp1 %>%
      select(hospital,
             product,
             sales_rep,
             sr_time,
             real_sr_time,
             promotional_budget,
             real_revenue,
             real_volume,
             hosp_code) %>%
      group_by(hospital) %>%
      dplyr::mutate(production_cost = sapply(product,function(x)product_info[which(product_info$类别==x),]$单位成本),
                    production_fee = round(production_cost*real_volume),
                    profit = real_revenue - promotional_budget - production_fee) %>%
      select(hospital,
             product,
             sales_rep,
             sr_time,
             real_sr_time,
             real_revenue,
             promotional_budget,
             production_fee,
             profit,
             hosp_code) %>%
      do(plyr::rbind.fill(.,data.frame(hospital = first(.$hospital),
                                       product = "总体",
                                       sales_rep = first(.$sales_rep),
                                       sr_time = sum(.$sr_time,na.rm=T),
                                       real_sr_time = sum(.$real_sr_time,na.rm=T),
                                       real_revenue = sum(.$real_revenue,na.rm=T),
                                       promotional_budget = sum(.$promotional_budget,na.rm=T),
                                       production_fee = sum(.$production_fee,na.rm=T),
                                       profit = sum(.$profit,na.rm=T),
                                       hosp_code = first(.$hosp_code)))) %>%
      ungroup()
    
    
    colnames(report4_mod1) <- c("医院",
                                "产品",
                                "销售代表",
                                "计划时间分配(天)",
                                "实际时间分配(天)",
                                "销售额(元)",
                                "推广费用(元)",
                                "生产成本(元)",
                                "利润(元)",
                                "hosp_code") 
    
    report4_mod1 <- report4_mod1 %>%
      gather(`因素`,value,`销售代表`,`计划时间分配(天)`,`实际时间分配(天)`,
             `销售额(元)`,`推广费用(元)`,`生产成本(元)`,`利润(元)`) %>%
      spread(`产品`,value) %>%
      left_join(report4_rank1,by="因素") %>%
      arrange(hosp_code,rank) %>%
      select(-rank,-hosp_code)
    
    
    
    ## report 5——1
    report5_mod1 <- tmp1 %>%
      select(hospital,
             hosp_code,
             product,
             real_revenue,
             pp_real_revenue,
             target_revenue)%>%
      group_by(hospital) %>%
      do(plyr::rbind.fill(.,data.frame(hospital = first(.$hospital),
                                       hosp_code = first(.$hosp_code),
                                       product = "总体",
                                       real_revenue=sum(.$real_revenue,na.rm=T),
                                       pp_real_revenue=sum(.$pp_real_revenue,na.rm=T),
                                       target_revenue = sum(.$target_revenue,na.rm=T)))) %>%
      dplyr::mutate(real_revenue_increase = real_revenue - pp_real_revenue,
                    real_revenue_increase_ratio = paste(round(real_revenue_increase/pp_real_revenue*100,2),"%",sep = ""),
                    target_revenue_realization = paste(round(real_revenue/target_revenue*100,2),"%",sep = "")) %>%
      arrange(hosp_code) %>%
      select(hospital, 
             product,
             target_revenue,
             pp_real_revenue,
             real_revenue,
             real_revenue_increase,
             real_revenue_increase_ratio,
             target_revenue_realization)
    
    
    
    colnames(report5_mod1) <- c("医院",
                                "产品",
                                "当期销售指标",
                                "上期销售额",
                                "当期销售额",
                                "销售额增长",
                                "销售额增长率",
                                "销售额达成率")
    
    
 
    report5_mod2 <- tmp1 %>%
      filter(sales_rep!=0) %>%
      select(sales_rep,
             product,
             real_revenue,
             pp_real_revenue,
             target_revenue) %>%
      group_by(sales_rep,product) %>%
      dplyr::summarise(real_revenue_by_sr = sum(real_revenue,na.rm=T),
                       pp_real_revenue_by_sr = sum(pp_real_revenue,na.rm=T),
                       target_revenue_by_sr = sum(target_revenue,na.rm=T)) %>%
      do(plyr::rbind.fill(.,data.frame(sales_rep=first(.$sales_rep),
                                       product ="总体",
                                       real_revenue_by_sr=sum(.$real_revenue_by_sr,na.rm=T),
                                       pp_real_revenue_by_sr =sum(.$pp_real_revenue_by_sr,na.rm=T),
                                       target_revenue_by_sr=sum(.$target_revenue_by_sr,na.rm=T)))) %>%
      dplyr::mutate(sr_target_revenue_realization = paste(round(real_revenue_by_sr/target_revenue_by_sr*100,2),"%",sep = "")) %>%
      select(sales_rep,
             product,
             target_revenue_by_sr,
             pp_real_revenue_by_sr,
             real_revenue_by_sr,
             sr_target_revenue_realization) %>%
      arrange(sales_rep)
    
    
    colnames(report5_mod2) <- c("销售代表",
                                "产品",
                                "当期销售指标",
                                "上期销售额",
                                "当期销售额",
                                "销售额达成率")
    
    report5_mod3 <- tmp1 %>%
      select(product,
             real_revenue,
             pp_real_revenue,
             target_revenue) %>%
      group_by(product) %>%
      dplyr::summarise(real_revenue_by_product = round(sum(real_revenue,na.rm=T)),
                       pp_real_revenue_by_product = round(sum(pp_real_revenue,na.rm=T)),
                       real_revenue_increase = round(real_revenue_by_product - pp_real_revenue_by_product),
                       target_revenue_by_product = round(sum(target_revenue,na.rm=T))) %>%
      do(plyr::rbind.fill(.,data.frame(product="总体",
                                       real_revenue_by_product=round(sum(.$real_revenue_by_product,na.rm=T)),
                                       pp_real_revenue_by_product=round(sum(.$pp_real_revenue_by_product,na.rm=T)),
                                       real_revenue_increase=sum(.$real_revenue_increase,na.rm=T),
                                       target_revenue_by_product=round(sum(.$target_revenue_by_product,na.rm=T))))) %>%
      dplyr::mutate(real_revenue_increase_ratio = ifelse(is.nan(round(real_revenue_increase/pp_real_revenue_by_product*100,2)),0,
                                                         paste(round(real_revenue_increase/pp_real_revenue_by_product*100,2),"%",sep = "")),
                    target_revenue_realization_by_product = ifelse(is.nan(round(real_revenue_by_product/target_revenue_by_product*100,2)),0,
                                                                   paste(round(real_revenue_by_product/target_revenue_by_product*100,2),"%",sep = ""))) %>%
      select(product,
             target_revenue_by_product,
             pp_real_revenue_by_product,
             real_revenue_by_product,
             real_revenue_increase,
             real_revenue_increase_ratio,
             target_revenue_realization_by_product)
    
    
    report5_mod3 <- report5_mod3 %>%
      left_join(product_info_list,by="product") %>%
      arrange(prod_code) %>%
      select(-prod_code)
    
    colnames(report5_mod3) <- c("产品",
                                "当期销售指标",
                                "上期销售额",
                                "当期销售额",
                                "销售额增长",
                                "销售额增长率",
                                "销售额达成率")
    
   
    
    
    out<-list("report1_mod1"=report1_mod1,
              "report1_mod2"=report1_mod2,
              "report2_mod1"=report2_mod1,
              "report2_mod2"=report2_mod2,
              "report2_mod3"=report2_mod3,
              "report2_mod4"=report2_mod4,
              "report2_mod5"=report2_mod5,
              "report3_mod1"=report3_mod1,
              "report3_mod2"=report3_mod2,
              "report4_mod1"=report4_mod1,
              "report5_mod1"=report5_mod1,
              "report5_mod2"=report5_mod2,
              "report5_mod3"=report5_mod3,
              "acc_success_value"=acc_success_value
              
    )
    
    out
    
  }
  
  data_to_use2 <- report_data(data_to_use,flm_data,last_report1_1)
  
  report1_1_tmp <- data_to_use2$report1_mod1 %>% 
    gather(variable,`值`,-phase) %>%
    spread(phase,`值`)  %>%
    left_join(report7_mod1_rank,by="variable") %>%
    arrange(rank) %>%
    select(-name,-rank)
  
  rownames(report1_1_tmp) <- report1_1_tmp$variable
  
  report1_1_tmp <- report1_1_tmp %>%
    select(-variable)
  
  if (phase==1){
    tmp_data <- list(
    "p1_report1_1" = report1_1_tmp,
    "p1_report1_2" = data_to_use2$report1_mod2,
    "p1_report2_1" = data_to_use2$report2_mod1,
    "p1_report2_2" = data_to_use2$report2_mod2,
    "p1_report2_3" = data_to_use2$report2_mod3,
    "p1_report2_4" = data_to_use2$report2_mod4,
    "p1_report2_5" = data_to_use2$report2_mod5,
    "p1_report3_1" = data_to_use2$report3_mod1,
    "p1_report3_2" = data_to_use2$report3_mod2,
    "p1_report4_1" = data_to_use2$report4_mod1,
    "p1_report5_1" = data_to_use2$report5_mod1,
    "p1_report5_2" = data_to_use2$report5_mod2,
    "p1_report5_3" = data_to_use2$report5_mod3,
    "p1_tmp" = data_to_use,
    "p1_report" = data_to_use2$report1_mod1,
    "p1_acc_success_value" = data_to_use2$acc_success_value)
  } else {
    tmp_data <- list(
    "p2_report1_1" = report1_1_tmp,
    "p2_report1_2" = data_to_use2$report1_mod2,
    "p2_report2_1" = data_to_use2$report2_mod1,
    "p2_report2_2" = data_to_use2$report2_mod2,
    "p2_report2_3" = data_to_use2$report2_mod3,
    "p2_report2_4" = data_to_use2$report2_mod4,
    "p2_report2_5" = data_to_use2$report2_mod5,
    "p2_report3_1" = data_to_use2$report3_mod1,
    "p2_report3_2" = data_to_use2$report3_mod2,
    "p2_report4_1" = data_to_use2$report4_mod1,
    "p2_report5_1" = data_to_use2$report5_mod1,
    "p2_report5_2" = data_to_use2$report5_mod2,
    "p2_report5_3" = data_to_use2$report5_mod3)
   }
   tmp_data
  
  
  ##----------------------------------------------------------------------------
  ##--               write the output results to the mongodb
  ##----------------------------------------------------------------------------
  
  #- create connection, database and collection
  
    mongodb_con <- mongo(collection = user_name,
        url = sprintf(
          "mongodb://%s/%s",
          # options()$mongodb$username,
          # options()$mongodb$password,
          options()$mongodb$host,
          "STP"))
  	
    mongodb_con$insert(tmp_data)
    
 
    
  
  
}, error = function(e) e)

#-- return the running status
if (inherits(exception_handle, what = "error")) {
  pass <- "FALSE"
} else {
  pass <- "TRUE"
}
