package com.company;

import java.util.Scanner;

public class Main
{
    public static int n = 1000,m = 1000;
    public static int [] _available = new int[m];
    public static int[][] _maximum = new int[n][m];
    public static int[][] _allocation = new int[n][m];
    public static int[][] _need = new int[n][m];

    public static void test(int[]available,int[][]maximum, int[][]allocation,int[][]need)
    {
        Scanner sc = new Scanner(System.in);
        int x = 0;
        System.out.println("Enter available resources in bank: ");
        for(int i=0;i<m;i++) {
            x = sc.nextInt();
            available[i] = x;
            _available[i] = x;
        }
        System.out.println("Enter maximum of resources for each each process: ");
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++) {
                x= sc.nextInt();
                maximum[i][j] = x;
                _maximum[i][j] = x;
            }
        }
        System.out.println("Enter allocation of resources for each process:");
        for(int i=0;i<n;i++) {
            for (int j = 0; j < m; j++){
                x = sc.nextInt();
                allocation[i][j] = x;
                _allocation[i][j] = x;
            }
        }
        for(int i=0;i<n;i++) {
            for (int j = 0; j < m; j++) {
                need[i][j] = maximum[i][j] - allocation[i][j];
                _need[i][j] = _maximum[i][j] - _allocation[i][j];
            }
        }
    }

    public static void Print(int[]available,int[][]need, int[][]allocation)
    {
        System.out.println("Available");
        for(int i =0;i<available.length;i++)
            System.out.println(available[i]);

        System.out.println("allocation");
        for(int i =0;i<need.length;i++)
        {
            for(int j=0;j<need[0].length;j++)
            {
                System.out.print(allocation[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("need");
        for(int i =0;i<need.length;i++)
        {
            for(int j=0;j<need[0].length;j++)
            {
                System.out.print(need[i][j]+" ");
            }
            System.out.println();
        }
    }
    public static boolean checkAvailable(int [][]need,int i,int [] available)
    {
        boolean check = true;

        for(int j=0;j<available.length;j++)
            if(need[i][j] > available[j] || need[i][j] == -1)
                check = false;
        return check;
    }
    public static void addAvailable(int[][]allocated,int i,int [] available)
    {
        for(int j=0;j<available.length;j++)
            available[j] += allocated[i][j];
    }
    public static void removeProcess(int[][]need,int i)
    {
        for(int j=0;j<need[0].length;j++)
            need[i][j] = -1;
    }
    public static boolean checkFinish(int[][]need)
    {
        boolean check = true;
        for(int i =0;i<need.length;i++)
            for (int j = 0; j < need[0].length; j++)
                if(need[i][j] != -1)
                    check = false;
        return check;
    }
    public static boolean checkSafe(int[]available,int[][]need, int[][]allocation)
    {
        boolean check = true;
        String sequence = "Safe sequence: ";
        int counter = 0;
        int i = 0;
        while(true)
        {
            if(checkAvailable(need,i,available))
            {
                addAvailable(allocation,i,available);
                removeProcess(need,i);
                //Print(available,need,allocation);
                sequence += ("p"+i+" ");
                counter = 0;
            }
            else
                counter++;

            i++;
            if(i==need.length)
                i = 0;
            if(counter > need.length)
            {
                check = false;
                break;
            }
            if(checkFinish(need)) {
                System.out.println(sequence);
                break;
            }
        }

        for(int k=0;k<n;k++)
        {
            for (int j = 0; j < m; j++)
            {
                need[k][j]=_need[k][j];
                available[j]=_available[j];
            }
        }
        return check;
    }
    public static boolean checkRQ(int p,int []request,int[]available,int[][]need, int[][]allocation)
    {
        boolean check = false;

        //if (Request < Need)
        for(int j = 0 ;j<available.length;j++)
            if(request[j] > need[p][j])
                return false;

        //if (Request < Available)
        for(int j = 0 ;j<available.length;j++)
            if(request[j] > available[j])
                return false;

        // pretend to allocate this request
        for(int j = 0 ;j<available.length;j++)
        {
            allocation[p][j] += request[j] ;
            need[p][j] -= request[j];
            available[j] -=request[j];
        }

        //then check if it is safe or not
        check = checkSafe(available,need,allocation);

        if(check == true)
        {
            //make changes in the original data as the request is approved :D
            for(int j = 0 ;j<available.length;j++)
            {
                _allocation[p][j] += request[j] ;
                _need[p][j] -= request[j];
                _available[j] -=request[j];

                allocation[p][j] += request[j] ;
                need[p][j] -= request[j];
                available[j] -=request[j];
            }
        }
        return check;
    }
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter no of processes: ");
        n = sc.nextInt();
        System.out.print("Enter no of resources: ");
        m = sc.nextInt();

        int [] available = new int[m];
        int[][] maximum = new int[n][m];
        int[][] allocation = new int[n][m];
        int[][] need = new int[n][m];


        test(available,maximum,allocation,need);

        boolean checkS =checkSafe(available,need,allocation);
        if(checkS)
            System.out.println("System is in safe state");
        else
        {
            System.out.println("System is in Unsafe state");
            return;
        }
        while(true)
        {
            System.out.print("Enter :");
            String input = sc.next();
            if (input.toLowerCase().equals("rq"))
            {
                int p = sc.nextInt();
                int []request = new int[m];
                for(int i = 0;i<m;i++)
                    request[i]= sc.nextInt();

                if(p >= n)
                {
                    System.out.println("You have only "+n+" processes !!");
                    continue;
                }

                boolean check = checkRQ(p,request, available, need, allocation);
                if (check == true)
                    System.out.println("Approve the request");
                else
                    System.out.println("Release the request");
            }
            if(input.toLowerCase().equals("quit"))
                break;
        }
    }
}