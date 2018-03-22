/**
* Name: 3Other
* Author: ben
* Description: 
* Tags: Tag1, Tag2, TagN
*/

model Other


global skills:[RSkill]{
	
	init{
		do startR;
		
		create people number: 10;
		write to_R_dataframe(people);
	}
	
}

species people {
	int energy;
}

experiment RJava type:gui{
	output{}
}

