package com.mycompany.time_constrained_symmetric_traveling_salesman_problem;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.concert.IloObjective;
import ilog.concert.IloObjectiveSense;
import ilog.cplex.IloCplex;

public class TSPTW_Model {

    protected IloCplex model;

    protected int n;
    protected double t0;
    protected double[][] travel_time;
    protected double[] LB;
    protected double[] UB;
    protected IloNumVar[] t;
    protected IloIntVar[][] y;

    TSPTW_Model(int problem_size, double t0, double[] LB, double[] UB, double[][] travel_time) throws IloException {
        this.n = problem_size;
        this.t0 = t0;
        this.travel_time = travel_time;
        this.LB = LB;
        this.UB = UB;
        this.model = new IloCplex();
        this.t = new IloNumVar[n + 1];// t_i= 0....n+1
        this.y = new IloIntVar[n][n];

    }
// create variables

    protected void addVariables() throws IloException {
        for (int i = 0; i <= n; i++) {
            t[i] = (IloNumVar) model.numVar(0, Float.MAX_VALUE, IloNumVarType.Float, "t[" + i + "]");

        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                 if(i!=j)
                y[i][j] = (IloIntVar) model.numVar(0, 1, IloNumVarType.Int, "y[" + i + "][" + j + "]");
            }

        }
    }

    //The following code creates the objective function for the problem
    protected void addObjective() throws IloException {
        IloLinearNumExpr objective = model.linearNumExpr();
        objective.addTerm(t[n], 1);// t_(n+1)
        objective.addTerm(t[0], -1);//t_0

        IloObjective Obj = model.addObjective(IloObjectiveSense.Minimize, objective);
    }

    //The following code creates the constraints for the problem.
    protected void addConstraints() throws IloException {
        // Constrain (2)
        for (int i = 1; i <= n - 1; i++) {
            IloLinearNumExpr expr_2 = model.linearNumExpr();
            expr_2.addTerm(t[i], 1);
            expr_2.addTerm(t[0], -1);
            model.addGe(expr_2, travel_time[0][i]);
        }
        // Constrain (3)
        for (int i = 1; i <= n - 1; i++) {
            IloLinearNumExpr expr_3 = model.linearNumExpr();
            expr_3.addTerm(t[n], 1);
            expr_3.addTerm(t[i], -1);
            model.addGe(expr_3, travel_time[i][0]);
        }
        
        
     
        
        // Constrain (4)
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
               if(i!=j){ 
                double M = UB[i]-LB[j]+travel_time[i][j];
                IloLinearNumExpr expr_4 = model.linearNumExpr();
                expr_4.addTerm(t[i], 1);
                expr_4.addTerm(t[j], -1);
                expr_4.addTerm(y[i][j],M);
                model.addGe(expr_4, travel_time[i][j]);
               }
            
            }
        }
//        // Constrain (5)

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(i!=j){
                double M=UB[i]-LB[j]+travel_time[i][j];
                IloLinearNumExpr expr_5 = model.linearNumExpr();

                expr_5.addTerm(t[j], 1);
                expr_5.addTerm(t[i], -1);
                expr_5.addTerm(y[i][j], -M);
                model.addGe(expr_5, travel_time[i][j] - M); 
                }
            }

        }

        // Constrain (6 lower bounds)
        for (int i = 1; i <= n - 1; i++) {
            model.addGe(t[i], LB[i]);
        }
        // Constain (6 upper bounds)
        for (int i = 1; i <= n - 1; i++) {
            model.addLe(t[i], UB[i]);
        }
        // Constrain (7)
        model.addEq(t[0], t0);

       
    }

    public void solveModel() throws IloException {
        addVariables();
        addObjective();
        addConstraints();
        model.exportModel("STSPTW_Problem.lp");

        model.solve();
         for (int i = 0; i < n; i++) {
                System.out.println("Node: " + i);
                for (int j = 0; j < n; j++) {
                    System.out.print(" " + travel_time[i][j] + " ");
                }
                System.out.println();
            }

        if (model.getStatus() == IloCplex.Status.Feasible
                | model.getStatus() == IloCplex.Status.Optimal) {
            System.out.println();
            System.out.println("Solution status = " + model.getStatus());
            System.out.println();
            System.out.println("Problem size: " + n);
            System.out.println("Matrix of time distances is");
           
            System.out.println();
            System.out.println("Makespan " + model.getObjValue());
            System.out.println();

            System.out.println("The variables t_{i} ");
            for (int i = 0; i <= n; i++) {
                System.out.println("----> Node: " + i + " is visited at time " + model.getValue(t[i]) + "<---" + t[i].getName());
            }

        } else {
            System.out.println();
            System.out.println("The problem status is: " + model.getStatus());
        }

    }
}


