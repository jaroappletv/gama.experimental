 /**
* Name: RJava example
* Author: HUYNH Quang Nghi  and Srirama
* Description: This is RJava example
* Tags: RJava
*/

model RJava


global {
	file Rcode<-text_file("r.txt");
	init {		
		create RJava;
	}
}

species RJava skills:[RSkill] {
	init{
		do startR();
		loop one_line over:Rcode.contents{
			write one_line;			
			write R_eval(one_line);
		}
	}
}
experiment toto type:gui{
	output{
	}
}