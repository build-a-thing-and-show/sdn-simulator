package org.batash.sdnsimulator;

import org.batash.sdnsimulator.core.Simulator;
import org.batash.sdnsimulator.core.utility.Statistics;
import org.springframework.web.bind.annotation.*;

@RestController
public class SimulatorController {

    private final Simulator simulator;

    public SimulatorController(Simulator simulator) {
        this.simulator = simulator;
    }

    @RequestMapping(value = "/run", method = { RequestMethod.GET, RequestMethod.POST })
    public String runSimulation() {
        // Example: setup a simple simulation
        simulator.createController("ctrl1");
        simulator.createSwitch("sw1");
        simulator.createHost("host1");
        simulator.createHost("host2");
        simulator.createLink("link1", "host1", "sw1", 1.0f, 1000.0f, 100, false);
        simulator.createLink("link2", "sw1", "host2", 1.0f, 1000.0f, 100, false);
        simulator.generateFlow("flow1", "host1", "host2", 1000, 0.0f, (short) 0);

        Statistics stats = simulator.run(0.0f, 100.0f);

        // Return a simple notification (customize as needed)
        return "Simulation finished. Stats: " + stats.toString();
    }
}