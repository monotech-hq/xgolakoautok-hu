
(ns app.components.frontend.item-list-table.views
    (:require [app.components.frontend.item-list-table.prototypes :as item-list-table.prototypes]
              [elements.api                                       :as elements]
              [random.api                                         :as random]
              [x.components.api                                   :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-list-table
  ; @param (keyword) table-id
  ; @param (map) table-props
  ;  {:body (metamorphic-content)(opt)
  ;   :header (metamorphic-content)(opt)}
  [table-id {:keys [body header]}]
  ; BUG#0781
  ; A {:table-layout :fixed} beállítás használatával a táblázat egyenlően osztja
  ; fel a szélességet azok a cellák között, amelyek nem rendelkeznek beállított
  ; szélesség értékkel.
  ; Ezért szükséges meghatározni a fix szélességű cellák width értékét!
  ;
  ; A thead elem a tbody elem után következik a DOM fában, így ha a thead elem
  ; sticky pozícionálású, akkor is a táblázat tartalma felett (z tengely) jelenik meg!
  [:table {:style {:table-layout "fixed" :width "100%"}}
          (if body   [:tbody [x.components/content table-id body]])
          (if header [:thead [x.components/content table-id header]])])

(defn component
  ; @param (keyword)(opt) table-id
  ; @param (map) table-props
  ;  {:body (metamorphic-content)(opt)
  ;   :header (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [item-list-table {...}]
  ;
  ; @usage
  ;  [item-list-table :my-item-list-table {...}]
  ([table-props]
   [component (random/generate-keyword) table-props])

  ([table-id table-props]
   (let [];table-props (item-list-table.prototypes/table-props-prototype table-props)
        [item-list-table table-id table-props])))
