#!/bin/sh

# Create EKS Cluster with managed node groups and AWS Fargate support
eksctl create cluster --name capstone --region eu-west-1 --managed
eksctl create fargateprofile --namespace default --cluster capstone --labels node-type=fargate

# Create a IAM Policy for the ALB Ingress Controller
aws iam create-policy --policy-name ALBIngressControllerIAMPolicy --policy-document file://kubernetes/iam-policy.json

# Deploy ALB Ingress Controller
kubectl apply -f kubernetes/alb-ingress-controller.yaml

#  Deploy order service
kubectl apply -f kubernetes/orders.yaml

#  Deploy catalog service
ubectl apply -f kubernetes/catalog.yaml
