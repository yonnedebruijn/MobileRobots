package state;

import org.joone.engine.*;
import org.joone.engine.learning.TeachingSynapse;
import org.joone.io.FileInputSynapse;
import org.joone.io.MemoryInputSynapse;
import org.joone.net.NeuralNet;
import org.joone.util.MonitorPlugin;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Yonne on 05/12/13.
 */
@SuppressWarnings("serial")
public class Controller extends MonitorPlugin implements NeuralNetListener{
	String train_file;
	
	Parameters p;
	
	LinearLayer input;
    SigmoidLayer hidden1;
    SigmoidLayer hidden2;
    LinearLayer output;
    Monitor m;
    
    double[] errors;
    int count = 0;
	
	boolean ready = false;
	
    /**
     * @todo Fitness system for training (collisions/distance travelled/or something else?
     * @todo Prevent collisions between bots
     * i.e. respond to the left/right/split/merge/etc inputs in the neural net
     * This is the neural network used by the controller
     */
    protected NeuralNet net;

    /**
     * This is the input synapse used to grab information from the network.
     * DirectSynapses work the best for this type of usage, that is, when a
     * single input pattern will be applied to the network.
     */
    protected DirectSynapse inputForRetrieval;
    
    DirectSynapse newIn;
    DirectSynapse newOut;
    
    /**
     * This is used to store the output that we want to train the network
     * towards
     */
    protected MemoryInputSynapse desiredNetworkOutput;
    /**
     * This is used to store the input that results in the desired output
     */
    protected MemoryInputSynapse inputForTraining;

    public void netStopped(NeuralNetEvent e) {
    	System.out.println("Training finished");
    	// store the training values.
    	try {
			PrintWriter w = new PrintWriter("errors_" + errors[20]+".txt", "UTF-8");
			for(int i = 0; i < count; i++)
	    	{
				w.write(errors[i]+";");
	    	}
			// done writing, close.
			w.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	p.trainingDone = true;
    }
    
    public void cicleTerminated(NeuralNetEvent e) {
    	Monitor mon = (Monitor)e.getSource();
    	errors[count] = mon.getGlobalError();
    	count++;
    	/* We want print the results every 1000 cycles */
    	if(mon.getCurrentCicle() % 100 == 0) System.out.println("at cycle" + mon.getCurrentCicle() + " - Error = " + mon.getGlobalError());
    }
    
    /**
     * Constructor
     * @param p		parameters class containing the to be used parameters
     */
    Controller(state.Parameters p) {
    	errors = new double[p.trainingCicles];
    	this.p = p;
        train_file = p.train_file;
        // create and set the layers.
        input = new LinearLayer();
        hidden1 = new SigmoidLayer();
        hidden2 = new SigmoidLayer();
        output = new LinearLayer();
        
        input.setRows(p.input_nodes);
        hidden1.setRows(p.input_nodes/2);
        hidden2.setRows(p.input_nodes/3);
        output.setRows(p.output_nodes);
        
        // the synapses.
        FullSynapse synapse_IH1 = new FullSynapse();
        FullSynapse synapse_H1H2 = new FullSynapse();
        FullSynapse synapse_H2H3 = new FullSynapse();
        FullSynapse synapse_H3O = new FullSynapse();
        
        input.addOutputSynapse(synapse_IH1);
        hidden1.addInputSynapse(synapse_IH1);
        
        hidden1.addOutputSynapse(synapse_H1H2);
        hidden2.addInputSynapse(synapse_H1H2);
        
        hidden2.addOutputSynapse(synapse_H2H3);
        output.addInputSynapse(synapse_H2H3);
        
        // set the monitor
        m = new Monitor();
        m.setLearningRate(0.4);
        m.setMomentum(0.2);
        
        // create the neural net
        this.net = new NeuralNet();
        this.net.addLayer(input, NeuralNet.INPUT_LAYER);
        this.net.addLayer(hidden1, NeuralNet.HIDDEN_LAYER);
        this.net.addLayer(hidden2, NeuralNet.HIDDEN_LAYER);
        //this.net.addLayer(hidden3, NeuralNet.HIDDEN_LAYER);
        this.net.addLayer(output, NeuralNet.OUTPUT_LAYER);
        
        // set monitor
        this.net.setMonitor(m);
        
        // add this as a listener.
        m.addNeuralNetListener(this);
    }

    /**
     * set a new neural network for the controller
     * @param c 	controller to get network from
     */
    Controller(Controller c) {
        this.net = c.getNet();
    }

    /**
     * Set a new neural network
     * @param net
     */
    Controller(NeuralNet net) {
        this.net = net;
    }


    /**
     * Get the used neural network
     * @return  RepastNeuralWrapper
     */
    public NeuralNet getNet() {
        return net;
    }
    
    
    /**
     * Train the neural network, optionally using information from the 'jungle'
     * @param  j 	jungle
     */
    public void train(Jungle j)
    {
    	// clear the jungle
    	j.clearPool();
    	
    	// set some coordinates and create some new agents
    	Coordinate ca = new Coordinate(220,220);
    	Coordinate cb = new Coordinate(500,500);
    	Coordinate cc = new Coordinate(0,0);

    	Agent a = new Agent(ca, p, 1, this, j.log, j.debug);
    	Agent b = new Agent(cb, p, 1, this, j.log, j.debug);
    	Agent c = new Agent(cc, p, 1, this, j.log, j.debug);
    	
    	// create start group
    	ArrayList<Agent> start_list = new ArrayList<Agent>();
    	start_list.add(a);
    	start_list.add(b);
    	start_list.add(c);
    	
    	// add group to jungle.
    	Group start_group = new Group(1, start_list, j.debug,j.log, j);
    	j.add_group(start_group);
    	j.set_focus(start_group.group_id);

    	train(a);

    }
    
    public void train(Agent a)
    {
    	System.out.println("training started");
    	// get the object that watches over the training
        
        FileInputSynapse is = new FileInputSynapse();
        is.setAdvancedColumnSelector("1-16");
        is.setFileName(train_file);
        this.net.getInputLayer().addInputSynapse(is);
        
        TeachingSynapse trainer = new TeachingSynapse();
        FileInputSynapse os = new FileInputSynapse();
        os.setFileName(train_file);
        trainer.setDesired(os);
        os.setAdvancedColumnSelector("17-20");
        trainer.setMonitor(m);
        
        this.net.getOutputLayer().addOutputSynapse(trainer);
        
        this.net.getMonitor().setTrainingPatterns(386);
        this.net.getMonitor().setTotCicles(p.trainingCicles);
        this.net.getMonitor().setLearning(true);


        // now actually train the network
        this.net.start();
        this.net.getMonitor().Go();
    }
    
    /**
     * Generate a move using the neural net, based on some p
     * The coordinate returned is used a direction to move toward,
     * the real new location is calculated based on this direction and param.step_size
     *
     * @param currentposition
     * @return NetworkResults
     */
    public NetworkResults generateMove(double[] inputs)
    {
    	//System.out.println(Arrays.toString(inputs));
    	// if we haven't done this after training, we should.
    	if(!ready)
    	{
    		System.out.println("not ready");
    		this.net.getMonitor().Stop();
    		this.net.getMonitor().setLearning(false);
    		this.net.getMonitor().Go();
    		
    		Layer in = this.net.getInputLayer();
			in.removeAllInputs();
			this.newIn = new DirectSynapse();
			in.addInputSynapse(this.newIn);
			
			Layer out = this.net.getOutputLayer();
			out.removeAllOutputs();
			this.newOut = new DirectSynapse();
			out.addOutputSynapse(this.newOut);
			this.net.start();
			ready = true;
			
    	}
		
    	// start the net, give it input values
    	Pattern ip = new Pattern(inputs);
    	ip.setCount(1);
        this.newIn.fwdPut(ip);

        // retrieve values and return them.
        Pattern op = this.newOut.fwdGet();
        //System.out.println(Arrays.toString(op.getValues()));
        return new NetworkResults(op.getValues()[0], op.getValues()[1], (int) Math.round(op.getValues()[2]), (int) op.getValues()[3]);
    }

	@Override
	protected void manageCycle(Monitor arg0) {
		System.out.println("cycle");
		// TODO Auto-generated method stub
	}

	@Override
	protected void manageError(Monitor arg0) {
		// TODO Auto-generated method stub
		if(ready) System.out.println("error");
	}

	@Override
	protected void manageStart(Monitor arg0) {
		System.out.println("start called");
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void manageStop(Monitor arg0) {
		System.out.println("stop");
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void manageStopError(Monitor arg0, String arg1) {
		System.out.println("stoperror");
		// TODO Auto-generated method stub
		
	}

}