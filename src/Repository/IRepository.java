package Repository;
import Model.State.PrgState;

import java.io.IOException;
import java.util.List;

public interface IRepository {
    PrgState getCurrentProgramState();
    void addProgramState(PrgState newProgramState);

    String getLogFilePath();

    void setProgramStates(List<PrgState> prgStates);

    List<PrgState> getProgramStates();

    void logProgramStateExecution(PrgState programState) throws IOException;
}
