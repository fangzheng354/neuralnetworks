package com.github.neuralnetworks.training;

import java.util.Map;

import com.github.neuralnetworks.architecture.Layer;
import com.github.neuralnetworks.architecture.NeuralNetwork;
import com.github.neuralnetworks.architecture.types.DNN;
import com.github.neuralnetworks.architecture.types.RBM;
import com.github.neuralnetworks.calculation.OutputError;
import com.github.neuralnetworks.calculation.RBMLayerCalculator;
import com.github.neuralnetworks.training.backpropagation.BackPropagationAutoencoder;
import com.github.neuralnetworks.training.backpropagation.BackPropagationFullyConnected;
import com.github.neuralnetworks.training.backpropagation.BackPropagationLayerCalculatorImpl;
import com.github.neuralnetworks.training.backpropagation.BackPropagationReLU;
import com.github.neuralnetworks.training.backpropagation.BackPropagationSigmoid;
import com.github.neuralnetworks.training.backpropagation.BackPropagationSoftReLU;
import com.github.neuralnetworks.training.backpropagation.BackPropagationTanh;
import com.github.neuralnetworks.training.backpropagation.BackPropagationTrainer;
import com.github.neuralnetworks.training.backpropagation.InputCorruptor;
import com.github.neuralnetworks.training.backpropagation.MSEDerivative;
import com.github.neuralnetworks.training.random.RandomInitializer;
import com.github.neuralnetworks.training.rbm.AparapiCDTrainer;
import com.github.neuralnetworks.training.rbm.DBNTrainer;
import com.github.neuralnetworks.util.Constants;
import com.github.neuralnetworks.util.Properties;

/**
 * Factory for trainers
 */
public class TrainerFactory {

    @SuppressWarnings("rawtypes")
    public static BackPropagationTrainer backPropagationSigmoid(NeuralNetwork nn, TrainingInputProvider trainingSet, TrainingInputProvider testingSet, OutputError error, RandomInitializer rand, float learningRate, float momentum, float weightDecay) {
	BackPropagationTrainer t = new BackPropagationTrainer(backpropProperties(nn, trainingSet, testingSet, error, rand, learningRate, momentum, weightDecay));

	BackPropagationLayerCalculatorImpl lc = new BackPropagationLayerCalculatorImpl();
	t.getProperties().setParameter(Constants.BACKPROPAGATION, lc);
	for (Layer l : nn.getLayers()) {
	    if (nn.getOutputLayer() != l) {
		if (nn.getInputLayer() != l) {
		    lc.addConnectionCalculator(l, new BackPropagationSigmoid(t.getProperties()));
		} else {
		    lc.addConnectionCalculator(l, new BackPropagationFullyConnected(t.getProperties()));
		}
	    }
	}

	return t;
    }
    
    @SuppressWarnings("rawtypes")
    public static BackPropagationTrainer backPropagationSoftReLU(NeuralNetwork nn, TrainingInputProvider trainingSet, TrainingInputProvider testingSet, OutputError error, RandomInitializer rand, float learningRate, float momentum, float weightDecay) {
	BackPropagationTrainer t = new BackPropagationTrainer(backpropProperties(nn, trainingSet, testingSet, error, rand, learningRate, momentum, weightDecay));

	BackPropagationLayerCalculatorImpl lc = new BackPropagationLayerCalculatorImpl();
	t.getProperties().setParameter(Constants.BACKPROPAGATION, lc);
	for (Layer l : nn.getLayers()) {
	    if (nn.getOutputLayer() != l) {
		if (nn.getInputLayer() != l) {
		    lc.addConnectionCalculator(l, new BackPropagationSoftReLU(t.getProperties()));
		} else {
		    lc.addConnectionCalculator(l, new BackPropagationFullyConnected(t.getProperties()));
		}
	    }
	}

	return t;
    }
    
    @SuppressWarnings("rawtypes")
    public static BackPropagationTrainer backPropagationReLU(NeuralNetwork nn, TrainingInputProvider trainingSet, TrainingInputProvider testingSet, OutputError error, RandomInitializer rand, float learningRate, float momentum, float weightDecay) {
	BackPropagationTrainer t = new BackPropagationTrainer(backpropProperties(nn, trainingSet, testingSet, error, rand, learningRate, momentum, weightDecay));
	
	BackPropagationLayerCalculatorImpl lc = new BackPropagationLayerCalculatorImpl();
	t.getProperties().setParameter(Constants.BACKPROPAGATION, lc);
	for (Layer l : nn.getLayers()) {
	    if (nn.getInputLayer() != l) {
		lc.addConnectionCalculator(l, new BackPropagationReLU(t.getProperties()));
	    } else {
		lc.addConnectionCalculator(l, new BackPropagationFullyConnected(t.getProperties()));
	    }
	}
	
	return t;
    }
    
    @SuppressWarnings("rawtypes")
    public static BackPropagationTrainer backPropagationTanh(NeuralNetwork nn, TrainingInputProvider trainingSet, TrainingInputProvider testingSet, OutputError error, RandomInitializer rand, float learningRate, float momentum, float weightDecay) {
	BackPropagationTrainer t = new BackPropagationTrainer(backpropProperties(nn, trainingSet, testingSet, error, rand, learningRate, momentum, weightDecay));

	BackPropagationLayerCalculatorImpl lc = new BackPropagationLayerCalculatorImpl();
	t.getProperties().setParameter(Constants.BACKPROPAGATION, lc);
	for (Layer l : nn.getLayers()) {
	    if (nn.getInputLayer() != l) {
		lc.addConnectionCalculator(l, new BackPropagationTanh(t.getProperties()));
	    } else {
		lc.addConnectionCalculator(l, new BackPropagationFullyConnected(t.getProperties()));
	    }
	}

	return t;
    }

    public static BackPropagationAutoencoder backPropagationSigmoidAutoencoder(NeuralNetwork nn, TrainingInputProvider trainingSet, TrainingInputProvider testingSet, OutputError error, RandomInitializer rand, InputCorruptor corruptor, float learningRate, float momentum, float weightDecay) {
	BackPropagationAutoencoder t = new BackPropagationAutoencoder(backpropProperties(nn, trainingSet, testingSet, error, rand, learningRate, momentum, weightDecay));
	t.setInputCorruptor(corruptor);

	BackPropagationLayerCalculatorImpl lc = new BackPropagationLayerCalculatorImpl();
	t.getProperties().setParameter(Constants.BACKPROPAGATION, lc);
	for (Layer l : nn.getLayers()) {
	    if (nn.getOutputLayer() != l) {
		lc.addConnectionCalculator(l, new BackPropagationSigmoid(t.getProperties()));
	    }
	}

	return t;
    }

    public static BackPropagationAutoencoder backPropagationSoftReLUAutoencoder(NeuralNetwork nn, TrainingInputProvider trainingSet, TrainingInputProvider testingSet, OutputError error, RandomInitializer rand, InputCorruptor corruptor, float learningRate, float momentum, float weightDecay) {
	BackPropagationAutoencoder t = new BackPropagationAutoencoder(backpropProperties(nn, trainingSet, testingSet, error, rand, learningRate, momentum, weightDecay));
	t.setInputCorruptor(corruptor);

	BackPropagationLayerCalculatorImpl lc = new BackPropagationLayerCalculatorImpl();
	t.getProperties().setParameter(Constants.BACKPROPAGATION, lc);
	for (Layer l : nn.getLayers()) {
	    if (nn.getOutputLayer() != l) {
		lc.addConnectionCalculator(l, new BackPropagationSoftReLU(t.getProperties()));
	    }
	}

	return t;
    }
    
    public static BackPropagationAutoencoder backPropagationReLUAutoencoder(NeuralNetwork nn, TrainingInputProvider trainingSet, TrainingInputProvider testingSet, OutputError error, RandomInitializer rand, InputCorruptor corruptor, float learningRate, float momentum, float weightDecay) {
	BackPropagationAutoencoder t = new BackPropagationAutoencoder(backpropProperties(nn, trainingSet, testingSet, error, rand, learningRate, momentum, weightDecay));
	t.setInputCorruptor(corruptor);
	
	BackPropagationLayerCalculatorImpl lc = new BackPropagationLayerCalculatorImpl();
	t.getProperties().setParameter(Constants.BACKPROPAGATION, lc);
	for (Layer l : nn.getLayers()) {
	    if (nn.getOutputLayer() != l) {
		lc.addConnectionCalculator(l, new BackPropagationReLU(t.getProperties()));
	    }
	}
	
	return t;
    }
    
    public static BackPropagationAutoencoder backPropagationTanhAutoencoder(NeuralNetwork nn, TrainingInputProvider trainingSet, TrainingInputProvider testingSet, OutputError error, RandomInitializer rand, InputCorruptor corruptor, float learningRate, float momentum, float weightDecay) {
	BackPropagationAutoencoder t = new BackPropagationAutoencoder(backpropProperties(nn, trainingSet, testingSet, error, rand, learningRate, momentum, weightDecay));
	t.setInputCorruptor(corruptor);

	BackPropagationLayerCalculatorImpl lc = new BackPropagationLayerCalculatorImpl();
	t.getProperties().setParameter(Constants.BACKPROPAGATION, lc);
	for (Layer l : nn.getLayers()) {
	    if (nn.getOutputLayer() != l) {
		lc.addConnectionCalculator(l, new BackPropagationTanh(t.getProperties()));
	    }
	}

	return t;
    }

    protected static Properties backpropProperties(NeuralNetwork nn, TrainingInputProvider trainingSet, TrainingInputProvider testingSet, OutputError error, RandomInitializer rand, float learningRate, float momentum, float weightDecay) {
	Properties p = new Properties();
	p.setParameter(Constants.NEURAL_NETWORK, nn);
	p.setParameter(Constants.TRAINING_INPUT_PROVIDER, trainingSet);
	p.setParameter(Constants.TESTING_INPUT_PROVIDER, testingSet);
	p.setParameter(Constants.LEARNING_RATE, learningRate);
	p.setParameter(Constants.MOMENTUM, momentum);
	p.setParameter(Constants.WEIGHT_DECAY, weightDecay);
	p.setParameter(Constants.LAYER_CALCULATOR, nn.getLayerCalculator());
	p.setParameter(Constants.OUTPUT_ERROR_DERIVATIVE, new MSEDerivative());
	p.setParameter(Constants.OUTPUT_ERROR, error);
	p.setParameter(Constants.RANDOM_INITIALIZER, rand);

	return p;
    }
    
    public static AparapiCDTrainer cdTrainer(RBM rbm, RBMLayerCalculator lc, TrainingInputProvider trainingSet, TrainingInputProvider testingSet, OutputError error, RandomInitializer rand, float learningRate, float momentum, float weightDecay, int gibbsSampling) {
	return new AparapiCDTrainer(rbmProperties(rbm, lc, trainingSet, testingSet, error, rand, learningRate, momentum, weightDecay, gibbsSampling, false));
    }

    public static AparapiCDTrainer pcdTrainer(RBM rbm, RBMLayerCalculator lc, TrainingInputProvider trainingSet, TrainingInputProvider testingSet, OutputError error, RandomInitializer rand, float learningRate, float momentum, float weightDecay, int gibbsSampling) {
	return new AparapiCDTrainer(rbmProperties(rbm, lc, trainingSet, testingSet, error, rand, learningRate, momentum, weightDecay, gibbsSampling, true));
    }

    protected static Properties rbmProperties(RBM rbm, RBMLayerCalculator lc, TrainingInputProvider trainingSet, TrainingInputProvider testingSet, OutputError error, RandomInitializer rand, float learningRate, float momentum, float weightDecay, int gibbsSampling, boolean resetRBM) {
	Properties p = new Properties();
	p.setParameter(Constants.NEURAL_NETWORK, rbm);
	p.setParameter(Constants.TRAINING_INPUT_PROVIDER, trainingSet);
	p.setParameter(Constants.TESTING_INPUT_PROVIDER, testingSet);
	p.setParameter(Constants.LAYER_CALCULATOR, new RBMLayerCalculator());
	p.setParameter(Constants.LEARNING_RATE, learningRate);
	p.setParameter(Constants.MOMENTUM, momentum);
	p.setParameter(Constants.WEIGHT_DECAY, weightDecay);
	p.setParameter(Constants.GIBBS_SAMPLING_COUNT, gibbsSampling);
	p.setParameter(Constants.OUTPUT_ERROR, error);
	p.setParameter(Constants.RANDOM_INITIALIZER, rand);
	p.setParameter(Constants.RESET_RBM, resetRBM);
	p.setParameter(Constants.LAYER_CALCULATOR, lc);

	return p;
    }

    public static DNNLayerTrainer dnnLayerTrainer(DNN<?> dnn, Map<NeuralNetwork, OneStepTrainer<?>> layerTrainers, TrainingInputProvider trainingSet, TrainingInputProvider testingSet, OutputError error) {
	return new DNNLayerTrainer(layerTrainerProperties(dnn, layerTrainers, trainingSet, testingSet, error));
    }

    public static DBNTrainer dbnTrainer(DNN<?> dnn, Map<NeuralNetwork, OneStepTrainer<?>> layerTrainers, TrainingInputProvider trainingSet, TrainingInputProvider testingSet, OutputError error) {
	return new DBNTrainer(layerTrainerProperties(dnn, layerTrainers, trainingSet, testingSet, error));
    }

    protected static Properties layerTrainerProperties(DNN<?> dnn, Map<NeuralNetwork, OneStepTrainer<?>> layerTrainers, TrainingInputProvider trainingSet, TrainingInputProvider testingSet, OutputError error) {
	Properties p = new Properties();
	p.setParameter(Constants.NEURAL_NETWORK, dnn);
	p.setParameter(Constants.TRAINING_INPUT_PROVIDER, trainingSet);
	p.setParameter(Constants.TESTING_INPUT_PROVIDER, testingSet);
	p.setParameter(Constants.LAYER_CALCULATOR, new RBMLayerCalculator());
	p.setParameter(Constants.OUTPUT_ERROR, error);
	p.setParameter(Constants.LAYER_TRAINERS, layerTrainers);

	return p;
    }
}
