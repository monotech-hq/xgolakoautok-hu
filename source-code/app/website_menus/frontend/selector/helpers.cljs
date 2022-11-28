
(ns app.website-menus.frontend.selector.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn import-id-f
  ; @param (namespaced map) menu-link
  ;  {:menu/id (string)}
  ;
  ; @example
  ;  (import-id-f {:menu/id "my-menu"})
  ;  =>
  ;  "my-menu"
  ;
  ; @return (string)
  [menu-link]
  (:menu/id menu-link))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn export-item-f
  ; @param (string) menu-id
  ; @param (map) menu-item
  ; @param (integer) menu-count
  ;
  ; @example
  ;  (export-item-f "my-menu" {...} 1)
  ;  =>
  ;  {:menu/id "my-menu"}
  ;
  ; @return (map)
  ;  {:menu/id (string)}
  [menu-id _ _]
  (if menu-id {:menu/id menu-id}))
