
(ns app.rental-vehicles.frontend.lister.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [layouts.surface-a.api       :as surface-a]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-lister-footer :rental-vehicles.lister {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-list-item
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ; @param (integer) item-dex
  ; @param (map) vehicle-item
  [_ _ item-dex {:keys [id modified-at name thumbnail]} {:keys [handle-attributes item-attributes]}]
  (let [timestamp @(r/subscribe [:x.activities/get-actual-timestamp modified-at])]
       [components/item-list-row {:cells [[components/list-item-gap         {:width 12}]
                                          [components/list-item-drag-handle {:indent {:left :xs} :drag-attributes handle-attributes}]
                                          [components/list-item-gap         {:width 12}]
                                          [components/list-item-thumbnail   {:thumbnail (:media/uri thumbnail)}]
                                          [components/list-item-gap         {:width 12}]
                                          [components/list-item-cell        {:rows [{:content name :placeholder :unnamed-rental-vehicle}]}]
                                          [components/list-item-gap         {:width 12}]
                                          [components/list-item-cell        {:rows [{:content timestamp :font-size :xs :color :muted}] :width 100}]
                                          [components/list-item-gap         {:width 12}]
                                          [components/list-item-button      {:label :open! :width 100 :on-click [:x.router/go-to! (str "/@app-home/rental-vehicles/"id)]}]
                                          [components/list-item-gap         {:width 12}]]
                                  :border (if (not= item-dex 0) :top)
                                  :drag-attributes item-attributes}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-list-header
  []
  [components/item-list-header ::vehicle-list-header
                               {:cells [{:width 12}
                                        {:width 24}
                                        {:width 12}
                                        {:width 84}
                                        {:width 12}
                                        {:label :name}
                                        {:width 12}
                                        {:label :modified :width 100}
                                        {:width 12}
                                        {:width 100}
                                        {:width 12}]
                                :border :bottom}])

(defn- vehicle-lister
  []
  [common/item-lister-body :rental-vehicles.lister
                           {:default-order-by  :order/ascending
                            :list-item-element #'vehicle-list-item
                            :item-list-header  #'vehicle-list-header
                            :items-path        [:rental-vehicles :lister/downloaded-items]
                            :on-order-changed  [:item-lister/reorder-items! :rental-vehicles.lister]
                            :sortable?         true}])

(defn- body
  []
  [components/surface-box ::body
                          {:content [:<> [vehicle-lister]
                                         [elements/horizontal-separator {:height :xxs}]]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  [common/item-lister-header :rental-vehicles.lister
                             {:crumbs    [{:label :app-home :route "/@app-home"}
                                          {:label :rental-vehicles}]
                              :on-create [:x.router/go-to! "/@app-home/rental-vehicles/create"]
                              :on-search [:item-lister/search-items! :rental-vehicles.lister {:search-keys [:name]}]
                              :search-placeholder :search-in-rental-vehicles
                              :label              :rental-vehicles}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
