package org.adaptlab.chpir.android.survey.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import org.adaptlab.chpir.android.survey.entities.Display;
import org.adaptlab.chpir.android.survey.entities.DisplayInstruction;

import java.util.List;

public class DisplayRelation {
    @Embedded
    public Display display;
    @Relation(parentColumn = "RemoteId", entityColumn = "DisplayRemoteId", entity = DisplayInstruction.class)
    public List<DisplayInstructionRelation> displayInstructions;
}