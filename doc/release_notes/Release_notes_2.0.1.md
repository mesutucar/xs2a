# Release notes v.2.0.1

## Bugfix: Fixed the process of checking daily access limit for AIS consent
From now on when TPP exceeds allowed frequency per day for AIS consent, it will receive response with `ACCESS_EXCEEDED` error (response code HTTP 429).

## Bugfix: made Tpp-Redirect-Uri to be mandatory for TPP-Redirect-Preferred == true case

From now, during the process of AIS consent creation and payment initiation, 
if `TPP-Redirect-Preferred` header is equal to `true`, the `TPP-Redirect-Uri` header is mandatory. 
In case of missing `TPP-Redirect-Uri` header, `400` HTTP error code will be returned.
