# API Specification

## Overview

This API manages gift cards, handling creation, balance updates, cancellation, balance exchanges, and bulk operations. The API communicates using JSON and follows a RESTful style.

## Base URL

http://localhost:8080/api/v1

## Authentication

Users must first register via `/auth/register`, then log in at `/auth/login` to receive a generated HS256 token.
By including this token in the `Authorization` header, users can access the `/api/v1/**` endpoints as authenticated clients.  

## Messaging

This project includes pubsub from GCP. You need to update the `spring.cloud.gcp.project-id` with a project with a pubsub
service account rights to write on topic `gift-card-topic`. For local run you have to set up the ADC keys on your machine.

## Notes

- All operations require valid card codes.
- Luhn algorithm used for secure code generation.
- GCP account is required for this project