import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Algorithm 
{
	//population of chromosomes
	//ArrayList of chromosome

	ArrayList<Schedule> chromosomes;
	
	
	//number of chromosomes which are replaced in each generation
	int replaceByGeneration; 
	
	//for creating new chromosomes
	Schedule schedule;
	
	int current_generation;
	int number_of_chromosomes;
	//this could be more structured according to object oriented , could use factory class to get rooms etc
	int total_rooms;
	
	ArrayList<Class> class_list;
	
	
	Algorithm (int number_of_chromosomes,int replace_by_generation,
			int track_best, int room,ArrayList<Class> class_list)
	{
		this.current_generation=0;
		this.replaceByGeneration=replace_by_generation;
		this.number_of_chromosomes=number_of_chromosomes;
		this.chromosomes=new ArrayList<Schedule>(number_of_chromosomes); 
		this.class_list=class_list;
		this.total_rooms=room;
		this.schedule=new Schedule(room);
		//this.schedule.makeChromosome(class_list, total_rooms);
	}
	
	void Start()
	{
		Random rand=new Random();

		this.schedule.makeChromosome(class_list, total_rooms);
		//create population
		for(int i=0;i<this.number_of_chromosomes;i++)
		{
			Schedule schedule1=new Schedule(this.total_rooms);
			Schedule newChromosome1=schedule1.makeChromosome(class_list,total_rooms);
			//add chromosome to the population
			
			//Schedule chromosome=this.schedule.makeChromosome(class_list, total_rooms);
			//System.out.println(newChromosome1.calculateFitness());
			this.chromosomes.add(newChromosome1);
			
			
		}
		
		
		this.current_generation=0;
		
		
		//start the algorithm
		
		
		while(this.current_generation<1000)
		{
			
			Schedule best=get_best_chromosome();
			System.out.println("Check best current: "+best.calculateFitness());
			if(best.calculateFitness() >= 0.95)
			{
				try {
					for(String batchfree:best.freeDaysForGroup)
					{
						System.out.println(batchfree);
					}
					best.WriteToExcel();
					//best.justCheckForFun();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//assume fitness
				System.out.println("best fitness: "+best.calculateFitness());
				break;
			}
			
			
			//produce offspring
			
			ArrayList<Schedule> offspring=new ArrayList<Schedule>();
			
			
			for(int j=0;j<this.replaceByGeneration;j++)
			{
				
				//select 2 parents randomly for crossover
				//int rand=(int) ( (Math.random()*this.chromosomes.size()) );
				
				Schedule p1=this.chromosomes.get(rand.nextInt(this.number_of_chromosomes));
				//Schedule p1=best;
				Schedule p2=this.chromosomes.get(rand.nextInt(this.number_of_chromosomes));
				//this.chromosomes[]
				
				
				//p1.cross_over(p2);
				//offspring.add(p1.cross_over(p2));
				offspring.add(j, p1.cross_over(p2));
				//System.out.println(offspring.get(j).calculateFitness());
				offspring.get(j).Mutation();
			}
			
			//replace chromosome 
			for(int j=0;j<this.replaceByGeneration;j++)
			{

				int ci=rand.nextInt(this.chromosomes.size());
				
				//dont add if new chromosome is not fittest or is already there
				
				
				
				if(best.calculateFitness()>=offspring.get(j).calculateFitness())
				{
					break;
				}
				
				
				//check if new chromosome does not replace current best chromosome
				//checkNewChromosome(offspring.get(j));
				float fitness=offspring.get(j).calculateFitness();
				for(int m=0;m<this.chromosomes.size();m++)
				{
					if(fitness>=this.chromosomes.get(m).calculateFitness())
					{
						//System.out.println("replacement");
						//should not replace from offspring need to check this
						//checkReplacement(offspring,this.chromosomes.get(m));
						
						//replace this
						
						this.chromosomes.remove(m);
						//System.out.println("p2");

						this.chromosomes.add(m, offspring.get(j));
					}
				}
			}
			
			
			
			this.current_generation++;
			
		}
		
	}
	
	

	void checkReplacement(ArrayList<Schedule> offspring, Schedule schedule2) 
	{
		
		
	}

	Schedule get_best_chromosome()
	{
		//please check this function
		float current_fitness=this.chromosomes.get(0).calculateFitness();
		Schedule best_chromosome=this.chromosomes.get(0);
		for(int i=1;i<this.chromosomes.size();i++)
		{
			if(this.chromosomes.get(i).calculateFitness()>=current_fitness)
			{
				//System.out.println(this.chromosomes.get(i));
				best_chromosome=this.chromosomes.get(i);
				//System.out.println(best_chromosome);
			}
		}
		
		//System.out.println(best_chromosome);
		return best_chromosome;
		
	}
}
