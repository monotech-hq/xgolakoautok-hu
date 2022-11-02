
(ns app.packages.frontend.selector.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-count-f
  ; @param (namespaced map) package-link
  ;  {:package/count (integer)}
  ;
  ; @example
  ;  (selector.helpers/import-count-f {:package/count 3 :package/id "my-package"})
  ;  =>
  ;  3
  ;
  ; @return (string)
  [package-link]
  (:package/count package-link))

(defn import-id-f
  ; @param (namespaced map) package-link
  ;  {:package/id (string)}
  ;
  ; @example
  ;  (selector.helpers/import-id-f {:package/count 3 :package/id "my-package"})
  ;  =>
  ;  "my-package"
  ;
  ; @return (string)
  [package-link]
  (:package/id package-link))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-item-f
  ; @param (string) package-id
  ; @param (map) package-item
  ; @param (integer) package-count
  ;
  ; @example
  ;  (selector.helpers/export-item-f "my-package" {...} 1)
  ;  =>
  ;  {:package/id "my-package" :package/count 1}
  ;
  ; @return (map)
  ;  {:package/count (integer)
  ;   :package/id (string)}
  [package-id _ package-count]
  (if package-id {:package/count package-count :package/id package-id}))
