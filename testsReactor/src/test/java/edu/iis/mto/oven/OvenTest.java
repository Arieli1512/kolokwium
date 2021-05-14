package edu.iis.mto.oven;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class OvenTest {

    @Mock
    private Fan fan;
    @Mock
    private HeatingModule heatingModule;

    private BakingProgram bakingProgram;
    private Oven oven;
    private List programStageList = new ArrayList();;
    @BeforeEach
    void setUp() throws Exception
    {
        oven = new Oven(heatingModule, fan);
        ProgramStage programStageOne =  ProgramStage.builder().withStageTime(5).withTargetTemp(100).withHeat(HeatType.THERMO_CIRCULATION).build();
        ProgramStage programStageTwo = ProgramStage.builder().withStageTime(10).withTargetTemp(180).withHeat(HeatType.HEATER).build();
        ProgramStage programStageThree =ProgramStage.builder().withStageTime(20).withTargetTemp(200).withHeat(HeatType.GRILL).build();
        programStageList.add(programStageOne);
        programStageList.add(programStageTwo);
        programStageList.add(programStageThree);
        bakingProgram = BakingProgram.builder().withInitialTemp(100).withStages(programStageList).build();
    }

    // Nie działa
    @Test
    void shouldThrowHeatingExceptionWhenHeatingSettingsCantBeGet() throws HeatingException {
        doThrow(HeatingException.class).when(heatingModule).termalCircuit(any(HeatingSettings.class));
        oven.start(bakingProgram);
        assertThrows(OvenException.class, () -> oven.start(bakingProgram));
    }

    @Test
    void heaterShouldBeInvokeTwoTimes()
    {
        oven.start(bakingProgram);
        verify(heatingModule,times(2)).heater(any());
    }
}
