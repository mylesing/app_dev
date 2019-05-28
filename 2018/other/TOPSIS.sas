

 options formdlim=' ' ls=90 ps=50;



        **************LOAD DECISION MATRIX***************;

data one;
input choice x1 x2 x3 x4 x5 x6;
cards;
1 2 1500 20000 5.5 5 9
2 2.5 2700 18000 6.5 3 5
3 1.8 2000 21000 4.5 7 7
4 2.2 1800 20000 5.0 5 5
;
run;


       ***********NORMALIZE IT*********************;


data two;
set one;
array info(6) x1-x6;
array binfo(6) x2_1-x2_6;
do i = 1 to 6;
binfo(i) = info(i)*info(i);
end;

keep choice x1-x6 x2_1-x2_6;
run;

proc print data=two;
quit;


proc univariate data=two;
var   x2_1-x2_6;
output out=x sum= sx2_1-sx2_6;
quit;


proc sql;
create table two as
select a.*, b.*
from two a left join x b
on a.choice;
quit;




data three;
set two;
array info(6) x1-x6;
array ainfo(6) r1-r6;
array binfo(6) sx2_1-sx2_6;
do i = 1 to 6;

ainfo(i) = info(i) / sqrt(binfo(i));
end;


keep choice r1-r6;
run;


proc print data=three;
quit;

                ******************LOAD CONSUMER PREFERENCE*****************;


data three;
set three;
w1=20;
w2=10;
w3=10;
w4=10;
w5=20;
w6=30;
run;

data three;
set three;
sum_w = sum(of w1-w6);
array info(*) w1-w6;
array binfo(*) wt1-wt6;
do i = 1 to dim(info);
binfo(i) = info(i) / sum_w;
end;

run;
proc print data = three;
quit;



              **************************WEIGHTED NORMALIZED***********;

data four;
set three;
array info(*) v1-v6;
array ainfo(*) r1-r6;
array binfo(*) wt1-wt6;

do i = 1 to  dim(info);


info(i) = ainfo(i)*binfo(i);
end;
drop i;
run;

proc print data=four;
var choice v1-v6;
quit;


      ****************************GET THE MAXIMIZED IDEAL and NEGATIVE SOLUTION*******;

proc univariate data=four noprint;
var v1-v6;
output out=z
max=vp1-vp6
min = vm1-vm6;
quit;

proc print data=z;
quit;


proc sql;
create table five as
select a.*, b.*
from four a left join z b
on a.choice;
quit;



        *************************SEPARATION MEASURES************;


data six;
set five;
*vp4= 0.0414;
*vm4 = 0.0598;



s_pos2 = 0;
s_neg2 = 0;
array info(*) v1-v6;
array binfo(*) vp1-vp6;
array ainfo(*) vm1-vm6;
do i = 1 to dim(info);
s_pos2 = s_pos2 + ((info(i) - binfo(i))**2);
s_neg2 = s_neg2 + ((info(i) - ainfo(i))**2);
end;
spos = sqrt(s_pos2);
sneg = sqrt(s_neg2);
run;


proc print data=z;
quit;


proc print data=six;
var Choice spos sneg;
quit;


                ******************CLOSENESS*************;

data seven;
set six;
c_star = sneg/(spos+sneg);
run;

proc sort data=seven;
by descending c_star ;
quit;

proc print data=seven;
id c_star;
var Choice spos sneg c_star;
quit;










     **************************PAUL^S EXAMPLE****************;





        **************LOAD DECISION MATRIX***************;

data one;
input choice x1 x2 x3 x4 x5 x6 x7 x8 x9;
cards;
1 50 30 60 85 80 80 80 0 0
2 50 30 80 70 60 70 80 0 0
3 50 10 100 100 100 100 100 100 100
;
run;

proc print data=one;
quit;



       ***********NORMALIZE IT*********************;


data two;
set one;
array info(9) x1-x9;
array binfo(9) x2_1-x2_9;
do i = 1 to 9;
binfo(i) = info(i)*info(i);
end;

keep choice x1-x9 x2_1-x2_9;
run;

proc print data=two;
quit;


proc univariate data=two;
var   x2_1-x2_9;
output out=x sum= sx2_1-sx2_9;
quit;


proc sql;
create table two as
select a.*, b.*
from two a left join x b
on a.choice;
quit;




data three;
set two;
array info(9) x1-x9;
array ainfo(9) r1-r9;
array binfo(9) sx2_1-sx2_9;
do i = 1 to 9;

ainfo(i) = info(i) / sqrt(binfo(i));
end;


keep choice r1-r9;
run;


proc print data=three;
quit;


ods rtf file='c:\example.rtf';



                ******************LOAD CONSUMER PREFERENCE-1*****************;


data three;
set three;
w1=100;
w2=100;
w3=100;
w4=30;
w5=30;
w6=30;
w7 =30 ;
w8 =80 ;
w9 =80 ;
run;

data three;
set three;
sum_w = sum(of w1-w9);
array info(*) w1-w9;
array binfo(*) wt1-wt9;
do i = 1 to dim(info);
binfo(i) = info(i) / sum_w;
end;

run;
*proc print data = three;
quit;



              **************************WEIGHTED NORMALIZED***********;

data four;
set three;
array info(*) v1-v9;
array ainfo(*) r1-r9;
array binfo(*) wt1-wt9;

do i = 1 to  dim(info);


info(i) = ainfo(i)*binfo(i);
end;
drop i;
run;

proc print data=four;
var choice v1-v9;
quit;


      ****************************GET THE MAXIMIZED IDEAL and NEGATIVE SOLUTION*******;

proc univariate data=four noprint;
var v1-v9;
output out=z
max=vp1-vp9
min = vm1-vm9;
quit;

*proc print data=z;
quit;


proc sql;
create table five as
select a.*, b.*
from four a left join z b
on a.choice;
quit;



        *************************SEPARATION MEASURES************;


data six;
set five;
*vp4= 0.0414;
*vm4 = 0.0598;



s_pos2 = 0;
s_neg2 = 0;
array info(*) v1-v9;
array binfo(*) vp1-vp9;
array ainfo(*) vm1-vm9;
do i = 1 to dim(info);
s_pos2 = s_pos2 + ((info(i) - binfo(i))**2);
s_neg2 = s_neg2 + ((info(i) - ainfo(i))**2);
end;
spos = sqrt(s_pos2);
sneg = sqrt(s_neg2);
run;


*proc print data=z;
quit;
          proc print data=z;
quit;

proc print data=six;
var Choice spos sneg;
quit;


                ******************CLOSENESS*************;

data seven;
set six;
c_star = sneg/(spos+sneg);
run;

proc sort data=seven;
by descending c_star ;
quit;

proc print data = three;
var w1-w9;
quit;

proc print data=seven;
id c_star;
var Choice spos sneg c_star;
quit;



           ******************LOAD CONSUMER PREFERENCE - 2*****************;


data three;
set three;
w1=100;
w2=80;
w3=0;
w4=0;
w5=70;
w6=0;
w7 =0 ;
w8 =60 ;
w9 =60 ;
run;

data three;
set three;
sum_w = sum(of w1-w9);
array info(*) w1-w9;
array binfo(*) wt1-wt9;
do i = 1 to dim(info);
binfo(i) = info(i) / sum_w;
end;

run;
*proc print data = three;
quit;



              **************************WEIGHTED NORMALIZED***********;

data four;
set three;
array info(*) v1-v9;
array ainfo(*) r1-r9;
array binfo(*) wt1-wt9;

do i = 1 to  dim(info);


info(i) = ainfo(i)*binfo(i);
end;
drop i;
run;

proc print data=four;
var choice v1-v9;
quit;


      ****************************GET THE MAXIMIZED IDEAL and NEGATIVE SOLUTION*******;

proc univariate data=four noprint;
var v1-v9;
output out=z
max=vp1-vp9
min = vm1-vm9;
quit;

*proc print data=z;
quit;


proc sql;
create table five as
select a.*, b.*
from four a left join z b
on a.choice;
quit;



        *************************SEPARATION MEASURES************;


data six;
set five;
*vp4= 0.0414;
*vm4 = 0.0598;



s_pos2 = 0;
s_neg2 = 0;
array info(*) v1-v9;
array binfo(*) vp1-vp9;
array ainfo(*) vm1-vm9;
do i = 1 to dim(info);
s_pos2 = s_pos2 + ((info(i) - binfo(i))**2);
s_neg2 = s_neg2 + ((info(i) - ainfo(i))**2);
end;
spos = sqrt(s_pos2);
sneg = sqrt(s_neg2);
run;


*proc print data=z;
quit;
               proc print data=z;
quit;

proc print data=six;
var Choice spos sneg;
quit;


                ******************CLOSENESS*************;

data seven;
set six;
c_star = sneg/(spos+sneg);
run;

proc sort data=seven;
by descending c_star ;
quit;

proc print data = three;
var w1-w9;
quit;
proc print data=seven;
id c_star;
var Choice spos sneg c_star;
quit;










           ******************LOAD CONSUMER PREFERENCE - 3*****************;


data three;
set three;
w1=100;
w2=50;
w3=0;
w4=20;
w5=0;
w6=100;
w7 =20 ;
w8 =0 ;
w9 =0 ;
run;

data three;
set three;
sum_w = sum(of w1-w9);
array info(*) w1-w9;
array binfo(*) wt1-wt9;
do i = 1 to dim(info);
binfo(i) = info(i) / sum_w;
end;

run;
*proc print data = three;
quit;



              **************************WEIGHTED NORMALIZED***********;

data four;
set three;
array info(*) v1-v9;
array ainfo(*) r1-r9;
array binfo(*) wt1-wt9;

do i = 1 to  dim(info);


info(i) = ainfo(i)*binfo(i);
end;
drop i;
run;

proc print data=four;
var choice v1-v9;
quit;


      ****************************GET THE MAXIMIZED IDEAL and NEGATIVE SOLUTION*******;

proc univariate data=four noprint;
var v1-v9;
output out=z
max=vp1-vp9
min = vm1-vm9;
quit;

*proc print data=z;
quit;


proc sql;
create table five as
select a.*, b.*
from four a left join z b
on a.choice;
quit;



        *************************SEPARATION MEASURES************;


data six;
set five;
*vp4= 0.0414;
*vm4 = 0.0598;



s_pos2 = 0;
s_neg2 = 0;
array info(*) v1-v9;
array binfo(*) vp1-vp9;
array ainfo(*) vm1-vm9;
do i = 1 to dim(info);
s_pos2 = s_pos2 + ((info(i) - binfo(i))**2);
s_neg2 = s_neg2 + ((info(i) - ainfo(i))**2);
end;
spos = sqrt(s_pos2);
sneg = sqrt(s_neg2);
run;


*proc print data=z;
quit;
      proc print data=z;
quit;

proc print data=six;
var Choice spos sneg;
quit;


                ******************CLOSENESS*************;

data seven;
set six;
c_star = sneg/(spos+sneg);
run;

proc sort data=seven;
by descending c_star ;
quit;

proc print data = three;
var w1-w9;
quit;
proc print data=seven;
id c_star;
var Choice spos sneg c_star;
quit;





           ******************LOAD CONSUMER PREFERENCE - 4*****************;


data three;
set three;
w1=100;
w2=100;
w3=80;
w4=0;
w5=10;
w6=10;
w7 =20 ;
w8 =20 ;
w9 =20 ;
run;

data three;
set three;
sum_w = sum(of w1-w9);
array info(*) w1-w9;
array binfo(*) wt1-wt9;
do i = 1 to dim(info);
binfo(i) = info(i) / sum_w;
end;

run;
*proc print data = three;
*quit;



              **************************WEIGHTED NORMALIZED***********;

data four;
set three;
array info(*) v1-v9;
array ainfo(*) r1-r9;
array binfo(*) wt1-wt9;

do i = 1 to  dim(info);


info(i) = ainfo(i)*binfo(i);
end;
drop i;
run;

proc print data=four;
var choice v1-v9;
quit;


      ****************************GET THE MAXIMIZED IDEAL and NEGATIVE SOLUTION*******;

proc univariate data=four noprint;
var v1-v9;
output out=z
max=vp1-vp9
min = vm1-vm9;
quit;

*proc print data=z;
*quit;


proc sql;
create table five as
select a.*, b.*
from four a left join z b
on a.choice;
quit;



        *************************SEPARATION MEASURES************;


data six;
set five;
*vp4= 0.0414;
*vm4 = 0.0598;



s_pos2 = 0;
s_neg2 = 0;
array info(*) v1-v9;
array binfo(*) vp1-vp9;
array ainfo(*) vm1-vm9;
do i = 1 to dim(info);
s_pos2 = s_pos2 + ((info(i) - binfo(i))**2);
s_neg2 = s_neg2 + ((info(i) - ainfo(i))**2);
end;
spos = sqrt(s_pos2);
sneg = sqrt(s_neg2);
run;


*proc print data=z;
*quit;
          proc print data=z;
quit;

proc print data=six;
var Choice spos sneg;
quit;


                ******************CLOSENESS*************;

data seven;
set six;
c_star = sneg/(spos+sneg);
run;

proc sort data=seven;
by descending c_star ;
quit;

proc print data = three;
var w1-w9;
quit;

proc print data=seven;
id c_star;
var Choice spos sneg c_star;
quit;


ods rtf close;






        *************************PAUL^S UPDATED EXAMPLE**********;





        **************LOAD DECISION MATRIX***************;

data one;
input choice x1 x2 x3 x4 x5 x6 x7 x8 x9;
cards;
1 50 30 60 85 80 80 80 100 100
2 50 30 80 70 60 70 80 100 100
3 50 10 100 100 100 100 100 0 0
;
run;

proc print data=one;
quit;



       ***********NORMALIZE IT*********************;


data two;
set one;
array info(9) x1-x9;
array binfo(9) x2_1-x2_9;
do i = 1 to 9;
binfo(i) = info(i)*info(i);
end;

keep choice x1-x9 x2_1-x2_9;
run;

proc print data=two;
quit;


proc univariate data=two;
var   x2_1-x2_9;
output out=x sum= sx2_1-sx2_9;
quit;


proc sql;
create table two as
select a.*, b.*
from two a left join x b
on a.choice;
quit;




data three;
set two;
array info(9) x1-x9;
array ainfo(9) r1-r9;
array binfo(9) sx2_1-sx2_9;
do i = 1 to 9;

ainfo(i) = info(i) / sqrt(binfo(i));
end;


keep choice r1-r9;
run;


proc print data=three;
quit;


ods rtf file='c:\example.rtf';



                ******************LOAD CONSUMER PREFERENCE-1*****************;


data three;
set three;
w1=100;
w2=100;
w3=100;
w4=30;
w5=30;
w6=30;
w7 =30 ;
w8 =80 ;
w9 =80 ;
run;

data three;
set three;
sum_w = sum(of w1-w9);
array info(*) w1-w9;
array binfo(*) wt1-wt9;
do i = 1 to dim(info);
binfo(i) = info(i) / sum_w;
end;

run;
*proc print data = three;
quit;



              **************************WEIGHTED NORMALIZED***********;

data four;
set three;
array info(*) v1-v9;
array ainfo(*) r1-r9;
array binfo(*) wt1-wt9;

do i = 1 to  dim(info);


info(i) = ainfo(i)*binfo(i);
end;
drop i;
run;

proc print data=four;
var choice v1-v9;
quit;


      ****************************GET THE MAXIMIZED IDEAL and NEGATIVE SOLUTION*******;

proc univariate data=four noprint;
var v1-v9;
output out=z
max=vp1-vp9
min = vm1-vm9;
quit;

*proc print data=z;
quit;


proc sql;
create table five as
select a.*, b.*
from four a left join z b
on a.choice;
quit;



        *************************SEPARATION MEASURES************;


data six;
set five;
*vp4= 0.0414;
*vm4 = 0.0598;



s_pos2 = 0;
s_neg2 = 0;
array info(*) v1-v9;
array binfo(*) vp1-vp9;
array ainfo(*) vm1-vm9;
do i = 1 to dim(info);
s_pos2 = s_pos2 + ((info(i) - binfo(i))**2);
s_neg2 = s_neg2 + ((info(i) - ainfo(i))**2);
end;
spos = sqrt(s_pos2);
sneg = sqrt(s_neg2);
run;


*proc print data=z;
quit;
          proc print data=z;
quit;

proc print data=six;
var Choice spos sneg;
quit;


                ******************CLOSENESS*************;

data seven;
set six;
c_star = sneg/(spos+sneg);
run;

proc sort data=seven;
by descending c_star ;
quit;

proc print data = three;
var w1-w9;
quit;

proc print data=seven;
id c_star;
var Choice spos sneg c_star;
quit;



           ******************LOAD CONSUMER PREFERENCE - 2*****************;


data three;
set three;
w1=100;
w2=80;
w3=0;
w4=0;
w5=70;
w6=0;
w7 =0 ;
w8 =60 ;
w9 =60 ;
run;

data three;
set three;
sum_w = sum(of w1-w9);
array info(*) w1-w9;
array binfo(*) wt1-wt9;
do i = 1 to dim(info);
binfo(i) = info(i) / sum_w;
end;

run;
*proc print data = three;
quit;



              **************************WEIGHTED NORMALIZED***********;

data four;
set three;
array info(*) v1-v9;
array ainfo(*) r1-r9;
array binfo(*) wt1-wt9;

do i = 1 to  dim(info);


info(i) = ainfo(i)*binfo(i);
end;
drop i;
run;

proc print data=four;
var choice v1-v9;
quit;


      ****************************GET THE MAXIMIZED IDEAL and NEGATIVE SOLUTION*******;

proc univariate data=four noprint;
var v1-v9;
output out=z
max=vp1-vp9
min = vm1-vm9;
quit;

*proc print data=z;
quit;


proc sql;
create table five as
select a.*, b.*
from four a left join z b
on a.choice;
quit;



        *************************SEPARATION MEASURES************;


data six;
set five;
*vp4= 0.0414;
*vm4 = 0.0598;



s_pos2 = 0;
s_neg2 = 0;
array info(*) v1-v9;
array binfo(*) vp1-vp9;
array ainfo(*) vm1-vm9;
do i = 1 to dim(info);
s_pos2 = s_pos2 + ((info(i) - binfo(i))**2);
s_neg2 = s_neg2 + ((info(i) - ainfo(i))**2);
end;
spos = sqrt(s_pos2);
sneg = sqrt(s_neg2);
run;


*proc print data=z;
quit;
               proc print data=z;
quit;

proc print data=six;
var Choice spos sneg;
quit;


                ******************CLOSENESS*************;

data seven;
set six;
c_star = sneg/(spos+sneg);
run;

proc sort data=seven;
by descending c_star ;
quit;

proc print data = three;
var w1-w9;
quit;
proc print data=seven;
id c_star;
var Choice spos sneg c_star;
quit;










           ******************LOAD CONSUMER PREFERENCE - 3*****************;


data three;
set three;
w1=100;
w2=50;
w3=0;
w4=20;
w5=0;
w6=100;
w7 =20 ;
w8 =0 ;
w9 =0 ;
run;

data three;
set three;
sum_w = sum(of w1-w9);
array info(*) w1-w9;
array binfo(*) wt1-wt9;
do i = 1 to dim(info);
binfo(i) = info(i) / sum_w;
end;

run;
*proc print data = three;
quit;



              **************************WEIGHTED NORMALIZED***********;

data four;
set three;
array info(*) v1-v9;
array ainfo(*) r1-r9;
array binfo(*) wt1-wt9;

do i = 1 to  dim(info);


info(i) = ainfo(i)*binfo(i);
end;
drop i;
run;

proc print data=four;
var choice v1-v9;
quit;


      ****************************GET THE MAXIMIZED IDEAL and NEGATIVE SOLUTION*******;

proc univariate data=four noprint;
var v1-v9;
output out=z
max=vp1-vp9
min = vm1-vm9;
quit;

*proc print data=z;
quit;


proc sql;
create table five as
select a.*, b.*
from four a left join z b
on a.choice;
quit;



        *************************SEPARATION MEASURES************;


data six;
set five;
*vp4= 0.0414;
*vm4 = 0.0598;



s_pos2 = 0;
s_neg2 = 0;
array info(*) v1-v9;
array binfo(*) vp1-vp9;
array ainfo(*) vm1-vm9;
do i = 1 to dim(info);
s_pos2 = s_pos2 + ((info(i) - binfo(i))**2);
s_neg2 = s_neg2 + ((info(i) - ainfo(i))**2);
end;
spos = sqrt(s_pos2);
sneg = sqrt(s_neg2);
run;


*proc print data=z;
quit;
      proc print data=z;
quit;

proc print data=six;
var Choice spos sneg;
quit;


                ******************CLOSENESS*************;

data seven;
set six;
c_star = sneg/(spos+sneg);
run;

proc sort data=seven;
by descending c_star ;
quit;

proc print data = three;
var w1-w9;
quit;
proc print data=seven;
id c_star;
var Choice spos sneg c_star;
quit;





           ******************LOAD CONSUMER PREFERENCE - 4*****************;


data three;
set three;
w1=100;
w2=50;
w3=80;
w4=80;
w5=0;
w6=90;
w7 =80 ;
w8 =0 ;
w9 =0 ;
run;

data three;
set three;
sum_w = sum(of w1-w9);
array info(*) w1-w9;
array binfo(*) wt1-wt9;
do i = 1 to dim(info);
binfo(i) = info(i) / sum_w;
end;

run;
*proc print data = three;
*quit;



              **************************WEIGHTED NORMALIZED***********;

data four;
set three;
array info(*) v1-v9;
array ainfo(*) r1-r9;
array binfo(*) wt1-wt9;

do i = 1 to  dim(info);


info(i) = ainfo(i)*binfo(i);
end;
drop i;
run;

proc print data=four;
var choice v1-v9;
quit;


      ****************************GET THE MAXIMIZED IDEAL and NEGATIVE SOLUTION*******;

proc univariate data=four noprint;
var v1-v9;
output out=z
max=vp1-vp9
min = vm1-vm9;
quit;

*proc print data=z;
*quit;


proc sql;
create table five as
select a.*, b.*
from four a left join z b
on a.choice;
quit;



        *************************SEPARATION MEASURES************;


data six;
set five;
*vp4= 0.0414;
*vm4 = 0.0598;



s_pos2 = 0;
s_neg2 = 0;
array info(*) v1-v9;
array binfo(*) vp1-vp9;
array ainfo(*) vm1-vm9;
do i = 1 to dim(info);
s_pos2 = s_pos2 + ((info(i) - binfo(i))**2);
s_neg2 = s_neg2 + ((info(i) - ainfo(i))**2);
end;
spos = sqrt(s_pos2);
sneg = sqrt(s_neg2);
run;


*proc print data=z;
*quit;
          proc print data=z;
quit;

proc print data=six;
var Choice spos sneg;
quit;


                ******************CLOSENESS*************;

data seven;
set six;
c_star = sneg/(spos+sneg);
run;

proc sort data=seven;
by descending c_star ;
quit;

proc print data = three;
var w1-w9;
quit;

proc print data=seven;
id c_star;
var Choice spos sneg c_star;
quit;


ods rtf close;
