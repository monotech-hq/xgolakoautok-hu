
(ns app.contents.frontend.selector.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-id-f
  ; @param (namespaced map) content-link
  ;  {:content/id (string)}
  ;
  ; @example
  ;  (selector.helpers/import-id-f {:content/id "my-content"})
  ;  =>
  ;  "my-content"
  ;
  ; @return (string)
  [content-link]
  (:content/id content-link))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-item-f
  ; @param (string) content-id
  ; @param (map) content-item
  ; @param (integer) content-count
  ;
  ; @example
  ;  (selector.helpers/export-item-f "my-content" 1)
  ;  =>
  ;  {:content/id "my-content"}
  ;
  ; @return (map)
  ;  {:content/id (string)}
  [content-id _ _]
  (if content-id {:content/id content-id}))
