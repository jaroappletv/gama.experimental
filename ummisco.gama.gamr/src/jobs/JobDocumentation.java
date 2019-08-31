package jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 *
 * @author damienphilippon Date : 19 Dec 2017 Abstract class used to do a Documentation job (will be derived by Project,
 *         ModelProject and ModelIndependent)
 */
public abstract class JobDocumentation extends WorkspaceJob {
	/**
	 * The directory that will be used to save the file(s)
	 */
	public String directory;

	/**
	 * Variable representing all the species and the link to the documentation files that will present them
	 */
	Map<String, String> speciesLink = new HashMap<>();
	/**
	 * Variable representing all the experiments and the link to the documentation files that will present them
	 */
	Map<String, String> experimentsLink = new HashMap<>();

	/**
	 * The list of all the models already done by the process
	 */
	ArrayList<String> modelsDone = new ArrayList<>();

	/**
	 * The path to the index file
	 */
	public String indexPath;

	/**
	 * Constructor of JobDocumentation that will use the given string as an output directory
	 * 
	 * @param directory
	 *            {@code String} the path (string) of the output directory
	 */
	public JobDocumentation(final String directory) {
		super(directory);
		this.directory = directory;
	}

	/**
	 * Method to run the job in background
	 */
	@Override
	public IStatus runInWorkspace(final IProgressMonitor monitor) throws CoreException {
		return Status.OK_STATUS;
	}

	/**
	 * Method to release the memory used by the variables of the job
	 */
	public void dispose() {
		this.experimentsLink = null;
		this.speciesLink = null;
		this.modelsDone = null;
	}

}
