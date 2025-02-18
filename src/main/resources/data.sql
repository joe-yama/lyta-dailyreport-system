-- employees
INSERT INTO daily_report_system.employees(code,name,role,password,delete_flg,created_at,updated_at)
     VALUES ("1","煌木　太郎","ADMIN","$2a$10$vY93/U2cXCfEMBESYnDJUevcjJ208sXav23S.K8elE/J6Sxr4w5jO",0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
INSERT INTO daily_report_system.employees(code,name,role,password,delete_flg,created_at,updated_at)
     VALUES ("2","田中　太郎","GENERAL","$2a$10$HPIjRCymeRZKEIq.71TDduiEotOlb8Ai6KQUHCs4lGNYlLhcKv4Wi",0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

-- reports
INSERT INTO daily_report_system.reports(report_date,title,content,employee_code,delete_flg,created_at,updated_at)
     VALUES ("2023-09-23","煌木　太郎の記載、タイトル","煌木　太郎の記載、内容","1",0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
INSERT INTO daily_report_system.reports(report_date,title,content,employee_code,delete_flg,created_at,updated_at)
     VALUES ("2023-09-23","田中　太郎の記載、タイトル","田中　太郎の記載、内容","2",0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
INSERT INTO daily_report_system.reports(report_date,title,content,employee_code,delete_flg,created_at,updated_at)
     VALUES ("2023-09-24","削除フラグtrueの日報タイトル","削除フラグtrueの日報タイトル","1",1,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
INSERT INTO daily_report_system.reports(report_date,title,content,employee_code,delete_flg,created_at,updated_at)
     VALUES ("2023-09-25","複数行の内容の日報タイトル","これは複数行の\n日報です。","1",0,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);