package org.camunda.bpm.unittest;

import org.camunda.bpm.engine.impl.cmmn.behavior.MilestoneActivityBehavior;
import org.camunda.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.engine.impl.cmmn.transformer.AbstractCmmnTransformListener;

/**
 * Adds a milestone to a CMMN model which can be triggered to force a reevaluation of all expressions in a case instance.
 *
 * @author Malte.Soerensen
 */
public class AddMilestoneTransformListener extends AbstractCmmnTransformListener {

    public static final String ID = "MILESTONE";

    @Override
    public void transformCasePlanModel(org.camunda.bpm.model.cmmn.impl.instance.CasePlanModel casePlanModel, CmmnActivity activity) {
        CmmnActivity milestone = activity.createActivity(ID);
        milestone.setActivityBehavior(new MilestoneActivityBehavior());
    }

}
