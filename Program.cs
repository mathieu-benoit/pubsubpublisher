using Grpc.Core;
using System;
using System.Text;

var marshaller = new Marshaller<string>(Encoding.UTF8.GetBytes, Encoding.UTF8.GetString);
var method = new Method<string, string>(MethodType.Unary, "test-service", "test-method", marshaller, marshaller);
var channel = new Channel("spanner.googleapis.com:443", ChannelCredentials.Insecure);
var callInvoker = channel.CreateCallInvoker();
var text = callInvoker.BlockingUnaryCall(method, "spanner.googleapis.com", new CallOptions(), "request");
Console.WriteLine(text);