package ssp.scheduleplanner.logic.commands;

import static java.util.Objects.requireNonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ssp.scheduleplanner.commons.core.EventsCenter;
import ssp.scheduleplanner.commons.events.ui.ChangeViewEvent;
import ssp.scheduleplanner.logic.CommandHistory;
import ssp.scheduleplanner.logic.commands.exceptions.CommandException;
import ssp.scheduleplanner.logic.parser.CliSyntax;
import ssp.scheduleplanner.model.Model;
import ssp.scheduleplanner.model.task.Date;
import ssp.scheduleplanner.model.task.Repeat;
import ssp.scheduleplanner.model.task.Task;

/**
 * Adds a repeated task to the Schedule Planner.
 */
public class AddRepeatCommand extends Command {
    public static final String COMMAND_WORD = "repeat";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a repeated task to the Schedule Planner. "
            + "Parameters: "
            + CliSyntax.PREFIX_REPEAT + "REPEAT"
            + CliSyntax.PREFIX_NAME + "NAME "
            + CliSyntax.PREFIX_DATE + "DATE "
            + CliSyntax.PREFIX_PRIORITY + "PRIORITY "
            + CliSyntax.PREFIX_VENUE + "VENUE "
            + "[" + CliSyntax.PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + CliSyntax.PREFIX_REPEAT + "10"
            + CliSyntax.PREFIX_NAME + "CS2103T Tutorial "
            + CliSyntax.PREFIX_DATE + "111018 "
            + CliSyntax.PREFIX_PRIORITY + "3 "
            + CliSyntax.PREFIX_VENUE + "COM1-0210 "
            + CliSyntax.PREFIX_TAG + "Tutorial";

    public static final String MESSAGE_SUCCESS = "New repeated task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the Schedule Planner.";

    private final Task toAdd;
    private final Repeat repeat;

    /**
     * Creates an AddRepeatCommand to add the specified {@code Task}
     */
    public AddRepeatCommand(Task task, Repeat times) {
        requireNonNull(task);
        toAdd = task;
        repeat = times;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        DateFormat schedulerFormat = new SimpleDateFormat("ddMMyy");
        for (int i = 0; i < Integer.parseInt(repeat.value); i++) {
            Calendar baseDate = toAdd.getDate().calendar;
            baseDate.add(Calendar.DAY_OF_YEAR, 7 * i);
            String newDate = schedulerFormat.format(baseDate);
            Date date = new Date(newDate);
            Task newTask = new Task(toAdd.getName(), date,
                    toAdd.getPriority(), toAdd.getVenue(), toAdd.getTags());

            if (model.hasTask(newTask)) {
                throw new CommandException(MESSAGE_DUPLICATE_TASK);
            }

            model.addTask(newTask);
        }
        model.commitSchedulePlanner();
        EventsCenter.getInstance().post(new ChangeViewEvent(ChangeViewEvent.View.NORMAL));

        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddRepeatCommand // instanceof handles nulls
                && toAdd.equals(((AddRepeatCommand) other).toAdd));
    }
}

