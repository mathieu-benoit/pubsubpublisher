using System;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;

var counter = 0;
var max = !string.IsNullOrEmpty(Environment.GetEnvironmentVariable("NUMBER_OF_MESSAGES")) ? Int32.Parse(Environment.GetEnvironmentVariable("NUMBER_OF_MESSAGES")) : 0;
var delay = !string.IsNullOrEmpty(Environment.GetEnvironmentVariable("DELAY_BETWEEN_MESSAGES")) ? Int32.Parse(Environment.GetEnvironmentVariable("DELAY_BETWEEN_MESSAGES")) : 0;
while (counter < max)
{
    Console.WriteLine("Hello, World!");
    counter++;
    await Task.Delay(delay);
}