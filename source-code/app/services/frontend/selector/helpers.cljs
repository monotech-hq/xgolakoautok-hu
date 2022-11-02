
(ns app.services.frontend.selector.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-count-f
  ; @param (namespaced map) service-link
  ;  {:service/count (integer)}
  ;
  ; @example
  ;  (selector.helpers/import-count-f {:service/count 3 :service/id "my-service"})
  ;  =>
  ;  3
  ;
  ; @return (string)
  [service-link]
  (:service/count service-link))

(defn import-id-f
  ; @param (namespaced map) service-link
  ;  {:service/id (string)}
  ;
  ; @example
  ;  (selector.helpers/import-id-f {:service/count 3 :service/id "my-service"})
  ;  =>
  ;  "my-service"
  ;
  ; @return (string)
  [service-link]
  (:service/id service-link))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-item-f
  ; @param (string) service-id
  ; @param (map) service-item
  ; @param (integer) service-count
  ;
  ; @example
  ;  (selector.helpers/export-item-f "my-service" {...} 1)
  ;  =>
  ;  {:service/id "my-service" :service/count 1}
  ;
  ; @return (map)
  ;  {:service/count (integer)
  ;   :service/id (string)}
  [service-id _ service-count]
  (if service-id {:service/count service-count :service/id service-id}))
