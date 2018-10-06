package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.model.SchedulePlanner;
import seedu.address.model.Model;

/**
 * Clears the Schedule Planner.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Your Schedule Planner has been cleared!";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.resetData(new SchedulePlanner());
        model.commitSchedulePlanner();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
