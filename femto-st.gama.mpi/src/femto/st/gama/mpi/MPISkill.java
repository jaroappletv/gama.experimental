package femto.st.gama.mpi;

import msi.gama.precompiler.IConcept;
import mpi.MPI;
import mpi.MPIException;
import msi.gama.precompiler.GamlAnnotations.action;
import msi.gama.precompiler.GamlAnnotations.arg;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.example;
import msi.gama.precompiler.GamlAnnotations.skill;
import msi.gama.precompiler.GamlAnnotations.var;
import msi.gama.precompiler.GamlAnnotations.vars;
import msi.gaml.types.IType;


@vars({ @var(name = IMPISkill.MPI_RANK, type = IType.INT, doc = @doc("get MPI RANK"))})
@skill(name=IMPISkill.MPI_NETWORK, concept = { IConcept.GUI, IConcept.COMMUNICATION, IConcept.SKILL })
public class MPISkill {
	
	@action(name = IMPISkill.MPI_RANK, args = {}, doc = @doc(value = "", returns = "", examples = { @example("") }))
	public int getMPIRANK()
	{
		//A toi de jouer
		
		return 0;
	}
	
}
