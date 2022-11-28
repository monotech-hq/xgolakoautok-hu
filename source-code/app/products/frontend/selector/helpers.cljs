
(ns app.products.frontend.selector.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-count-f
  ; @param (namespaced map) product-link
  ;  {:product/count (integer)}
  ;
  ; @example
  ;  (import-count-f {:product/count 3 :product/id "my-product"})
  ;  =>
  ;  3
  ;
  ; @return (string)
  [product-link]
  (:product/count product-link))

(defn import-id-f
  ; @param (namespaced map) product-link
  ;  {:product/id (string)}
  ;
  ; @example
  ;  (import-id-f {:product/count 3 :product/id "my-product"})
  ;  =>
  ;  "my-product"
  ;
  ; @return (string)
  [product-link]
  (:product/id product-link))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-item-f
  ; @param (string) product-id
  ; @param (map) product-item
  ; @param (integer) product-count
  ;
  ; @example
  ;  (export-item-f "my-product" {...} 1)
  ;  =>
  ;  {:product/id "my-product" :product/count 1}
  ;
  ; @return (map)
  ;  {:product/count (integer)
  ;   :product/id (string)}
  [product-id _ product-count]
  (if product-id {:product/count product-count :product/id product-id}))
