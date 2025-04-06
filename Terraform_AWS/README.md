# Assignment -3

## AWS Networking Setup with Terraform & GitHub Actions

## Overview
This Assignment sets up an AWS networking infrastructure using Terraform and implements Continuous Integration (CI) with GitHub Actions. The setup includes:
- Creating a Virtual Private Cloud (VPC) with public and private subnets across multiple availability zones.
- Configuring an Internet Gateway and route tables.
- Automating infrastructure deployment using Terraform.
- Implementing CI with GitHub Actions to enforce Terraform code quality

## Prerequisites

Before using the Terraform configuration files, make sure you have the following installed:

1.⁠ ⁠AWS CLI

   Install and configure the AWS Command Line Interface (CLI) to interact with AWS resources. For installation and configuration instructions, refer to [AWS CLI Documentation](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html).

2.⁠ ⁠Terraform

   Install Terraform, a tool to manage AWS infrastructure as code. Follow the instructions on the [Terraform website](https://www.terraform.io/downloads.html).


## AWS Networking Setup

### VPC and Subnets
•⁠  ⁠Create a *VPC* with 3 *public subnets* and 3 *private subnets* across 3 different availability zones in the same region.
•⁠  ⁠Each availability zone has 1 public and 1 private subnet.

### Internet Gateway
•⁠  ⁠Create an *Internet Gateway* (IGW) and attach it to the VPC.

### Route Tables
•⁠  ⁠Create a *public route table* and attach all public subnets to it.

•⁠  ⁠Create a *private route table* and attach all private subnets to it.

•⁠  ⁠Add a public route in the *public route table* with the destination CIDR block ⁠ 0.0.0.0/0 ⁠ and the IGW as the target.

## Steps to Deploy

### Step 1: Initialize Terraform

Run the following command to initialize the Terraform environment:

⁠ bash
terraform init
 ⁠

This will install the necessary providers and modules.

### Step 2: Apply Terraform Configuration

To apply the configuration and create the infrastructure, run:

⁠ bash
terraform apply
 ⁠

Terraform will prompt you to confirm the creation of resources. Type ⁠ yes ⁠ to proceed.

### Step 3: Verify Infrastructure

Once the deployment is complete, you can log in to the AWS Management Console to verify the following:

•⁠  ⁠The VPC and subnets are created.

•⁠  ⁠The Internet Gateway is attached to the VPC.

•⁠  ⁠The route tables are set up and associated with the respective subnets.

## Clean Up

To destroy the infrastructure and remove all the resources created by Terraform, run the following command:

> terraform destroy
 
