/**
* Name: Testconnection
* Author: ben
* Description: 
* Tags: Tag1, Tag2, TagN
*/

model Testconnection

global skills:[RSkill]{
	file Rcode<-text_file("r.txt");
	
	init{
		do startR;
	/* 	write R_eval("x<-1");
		loop s over:Rcode.contents{
			unknown a<- R_eval(s);
			write "R>"+s;
			write a;
		}*/
	}
	
}
experiment RJava type:gui{
	output{
	}
}
