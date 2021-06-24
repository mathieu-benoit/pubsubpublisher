using Microsoft.AspNetCore;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
 
WebHost.CreateDefaultBuilder()
    .Configure(app => app.Run(c => c.Response.WriteAsync("Hello, World!")))
    .Build().Run();