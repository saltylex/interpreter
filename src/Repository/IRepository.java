package Repository;

import Model.State.PrgState;

import java.io.IOException;
import java.util.List;

public interface IRepository {
    /* DEPRECATED BECAUSE OF MULTITHREADING
    PrgState getCurrentProgramState();
    */

    List<PrgState> getProgramStates();

    void setProgramStates(List<PrgState> prgStates);

    void addProgramState(PrgState newProgramState);

    void logProgramStateExecution(PrgState programState) throws IOException;
}
