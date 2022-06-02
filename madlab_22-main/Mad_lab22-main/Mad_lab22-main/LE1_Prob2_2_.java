public class Main
{
  public static void main (String[]args)
  {
    int a = Integer.parseInt (args[0]);
    for (int i = 1; i <= a; i++)
      {
          for(int s=1;s<=i;s++)
	      {
	          System.out.print(" ");
	      }
	      for (int j = i; j <= a; j++)
	      {
	          
              System.out.print(" *");
	      }
	      
	      System.out.println("");
      }

  }
}
