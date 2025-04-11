# API Specification

## Overview

This API manages gift cards, handling creation, balance updates, cancellation, balance exchanges, and bulk operations. The API communicates using JSON and follows a RESTful style.

## Base URL

http://localhost:8080/api/v1

## Authentication

_No authentication is currently required._

## Notes

- All operations require valid card codes.
- Luhn algorithm used for secure code generation.