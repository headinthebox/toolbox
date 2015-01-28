package eu.amidst.core.learning;

import COM.hugin.HAPI.ExceptionHugin;
import eu.amidst.core.database.DataBase;
import eu.amidst.core.database.filereaders.StaticDataOnDiskFromFile;
import eu.amidst.core.database.filereaders.arffFileReader.ARFFDataReader;
import eu.amidst.core.models.BayesianNetwork;
import eu.amidst.core.models.BayesianNetworkLoader;
import eu.amidst.core.models.BayesianNetworkWriter;
import eu.amidst.core.utils.BayesianNetworkGenerator;
import eu.amidst.core.utils.BayesianNetworkSampler;
import eu.amidst.core.variables.Variable;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * Created by Hanen on 28/01/15.
 */
public class MLGenratedBNTest {

    @Test
    public void testingMLGeneratedBN() throws ExceptionHugin, IOException, ClassNotFoundException {


        BayesianNetworkGenerator.setNumberOfContinuousVars(10);
        BayesianNetworkGenerator.setNumberOfDiscreteVars(1);
        BayesianNetworkGenerator.setNumberOfStates(2);

        BayesianNetwork naiveBayes = BayesianNetworkGenerator.generateNaiveBayes(new Random(0), 2);
        System.out.println(naiveBayes.toString());

        //Sampling
        BayesianNetworkSampler sampler = new BayesianNetworkSampler(naiveBayes);
        sampler.setSeed(0);
        sampler.setParallelMode(true);

        DataBase data = sampler.sampleToDataBase(10000000);


        //Parameter Learning
        MaximumLikelihood.setBatchSize(1000);
        MaximumLikelihood.setParallelMode(true);
        BayesianNetwork bnet = MaximumLikelihood.learnParametersStaticModel(naiveBayes.getDAG(), data);

        //Check the probability distributions of each node
        for (Variable var : naiveBayes.getStaticVariables()) {
            System.out.println("\n------ Variable " + var.getName() + " ------");
            System.out.println("\nTrue distribution:\n"+ naiveBayes.getDistribution(var));
            System.out.println("\nLearned distribution:\n"+ bnet.getDistribution(var));
            assertTrue(bnet.getDistribution(var).equalDist(naiveBayes.getDistribution(var), 0.05));
        }

        //Or check directly if the true and learned networks are equals
        assertTrue(bnet.equalBNs(naiveBayes,0.05));
    }

}