
(ns app.clients.frontend.selector.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-id-f
  ; @param (namespaced map) client-link
  ;  {:client/id (string)}
  ;
  ; @example
  ;  (import-id-f {:client/id "my-client"})
  ;  =>
  ;  "my-client"
  ;
  ; @return (string)
  [client-link]
  (:client/id client-link))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-item-f
  ; @param (string) client-id
  ; @param (map) client-item
  ; @param (integer) client-count
  ;
  ; @example
  ;  (export-item-f "my-client" {...} 1)
  ;  =>
  ;  {:client/id "my-client"}
  ;
  ; @return (map)
  ;  {:client/id (string)}
  [client-id _ _]
  (if client-id {:client/id client-id}))
