package org.camunda.bpm.unittest;

import static org.assertj.core.api.Assertions.assertThat;

import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricCaseActivityInstance;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@Deployment(resources = "TestCase.cmmn")
public class AddMilestoneTransformListenerTest {

    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    private CaseInstance caseInstance;

    @Before
    public void startCase() {
        caseInstance = caseService().createCaseInstanceByKey("TestCase");
        Assume.assumeTrue(caseInstance.isActive());
    }


    @Test
    public void milestone_should_have_occured() {
        HistoricCaseActivityInstance historicMilestone = historyService().createHistoricCaseActivityInstanceQuery().caseActivityId(AddMilestoneTransformListener.ID).singleResult();
        assertThat(historicMilestone).isNotNull();
        assertThat(historicMilestone.isCompleted()).isNotNull();
    }

    private HistoryService historyService() {return processEngineRule.getHistoryService();}

    private CaseService caseService() {
        return processEngineRule.getCaseService();
    }

}
