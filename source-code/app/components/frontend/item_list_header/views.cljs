
(ns app.components.frontend.item-list-header.views
    (:require [app.components.frontend.item-list-header.helpers    :as item-list-header.helpers]
              [app.components.frontend.item-list-header.prototypes :as item-list-header.prototypes]
              [elements.api                                        :as elements]
              [random.api                                          :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-separator
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;  {:cells (vector)}
  [_ {:keys [border cells]}]
  ; XXX#0611
  ; Fontos, hogy a header magassága szabványos legyen (pl. 48px),
  ; ezért a separator magasságából szükséges kivonni a border magsságát!
  [:tr {:style {:height (if border "11px" "12px")}}
       (letfn [(f [cells _] (conj cells [:th]))]
              (reduce f [:<>] cells))])

(defn- header-border
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;  {:cells (vector)}
  [_ {:keys [cells]}]
  ; XXX#0611
  [:tr {:style {:height "1px" :background-color "#f0f0f0" :position "sticky" :top "84px"}}
       (letfn [(f [cells _] (conj cells [:th]))]
              (reduce f [:<>] cells))])

(defn- header-cell
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; @param (integer) cell-dex
  ; @param (map) cell-props
  ;  {:label (metamorphic-content)
  ;   :on-click (metamorphic-event)(opt)}
  [header-id header-props cell-dex {:keys [label on-click] :as cell-props}]
  [:th (item-list-header.helpers/header-cell-attributes header-id header-props cell-dex cell-props)
       (let [cell-props (item-list-header.prototypes/cell-props-prototype header-id header-props cell-dex cell-props)]
            [:div {:style {:display "flex"}}
                  (if on-click [elements/button cell-props]
                               [elements/label  cell-props])])])

(defn- item-list-header
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;  {:border (keyword)(opt)
  ;   :cells (maps in vector)}
  [header-id {:keys [border cells] :as header-props}]
  [:<> (if (= border :top) [header-border header-id header-props])
       [:tr (item-list-header.helpers/header-attributes header-id header-props)
            (letfn [(f [cells cell-dex cell-props] (conj cells [header-cell header-id header-props cell-dex cell-props]))]
                   (reduce-kv f [:<>] cells))]
       (if (= border :bottom) [header-border header-id header-props])
       [header-separator header-id header-props]])

(defn component
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ;  {:border (keyword)(opt)
  ;    :bottom, :top}
  ;   :cells (maps in vector)
  ;    [{:label (metamorphic-content)
  ;      :width (px)(opt)}]}
  ;
  ; @usage
  ;  [item-list-header {...}]
  ;
  ; @usage
  ;  [item-list-header :my-item-list-header {...}]
  ;
  ; @usage
  ;  [item-list-header :my-item-list-header {:cells [[:div ]]}]
  ([header-props]
   [component (random/generate-keyword) header-props])

  ([header-id header-props]
   (let [header-props (item-list-header.prototypes/header-props-prototype header-props)]
        [item-list-header header-id header-props])))
