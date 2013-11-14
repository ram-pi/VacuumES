package agent;

import java.awt.Point;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import core.LocalVacuumEnvironmentPercept;

public class VacuumAgent1 extends AbstractAgent {

	public VacuumAgent1() {
		super(new AgentProgram() {
			@Override
			public Action execute(final Percept percept) {
				final LocalVacuumEnvironmentPercept vep = (LocalVacuumEnvironmentPercept) percept;
				final Set<Action> actionsKeySet = vep.getActionEnergyCosts()
						.keySet();
				new Random();
				final int randomInt = new Random()
						.nextInt(actionsKeySet.size());
				final Iterator<Action> iterator = actionsKeySet.iterator();
				for (int i = 0; i < randomInt; i++)
					iterator.next();
				Point myloc = vep.getAgentLocation();
				System.out.println(myloc);;
				return iterator.next();
			}
		});
	}
}
