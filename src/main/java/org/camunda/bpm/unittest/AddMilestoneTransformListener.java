package org.camunda.bpm.unittest;

import org.camunda.bpm.engine.impl.cmmn.behavior.MilestoneActivityBehavior;
import org.camunda.bpm.engine.impl.cmmn.handler.ItemHandler;
import org.camunda.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.engine.impl.cmmn.transformer.AbstractCmmnTransformListener;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.impl.history.event.HistoryEventTypes;
import org.camunda.bpm.engine.impl.history.producer.CmmnHistoryEventProducer;
import org.camunda.bpm.engine.impl.history.transformer.CaseActivityInstanceCreateListener;
import org.camunda.bpm.engine.impl.history.transformer.CaseActivityInstanceEndListener;
import org.camunda.bpm.engine.impl.history.transformer.CaseActivityInstanceUpdateListener;

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
        addMilestoneHandlers(milestone);
    }

    protected void addMilestoneHandlers(CmmnActivity caseActivity) {
        HistoryLevel historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
        CmmnHistoryEventProducer historyEventProducer = Context.getProcessEngineConfiguration().getCmmnHistoryEventProducer();

        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.CASE_ACTIVITY_INSTANCE_CREATE, null)) {
            for (String event : ItemHandler.EVENT_LISTENER_OR_MILESTONE_CREATE_EVENTS) {
                caseActivity.addListener(event, new CaseActivityInstanceCreateListener(historyEventProducer, historyLevel));
            }
        }
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.CASE_ACTIVITY_INSTANCE_UPDATE, null)) {
            for (String event : ItemHandler.EVENT_LISTENER_OR_MILESTONE_UPDATE_EVENTS) {
                caseActivity.addListener(event, new CaseActivityInstanceUpdateListener(historyEventProducer, historyLevel));
            }
        }
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.CASE_ACTIVITY_INSTANCE_END, null)) {
            for (String event : ItemHandler.EVENT_LISTENER_OR_MILESTONE_END_EVENTS) {
                caseActivity.addListener(event, new CaseActivityInstanceEndListener(historyEventProducer, historyLevel));
            }
        }
    }

}
