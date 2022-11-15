
(ns app.services.frontend.viewer.views
    (:require [app.common.frontend.api  :as common]
              [app.storage.frontend.api :as storage]
              [elements.api             :as elements]
              [engines.item-lister.api  :as item-lister]
              [engines.item-viewer.api  :as item-viewer]
              [forms.api                :as forms]
              [layouts.surface-a.api    :as surface-a]
              [re-frame.api             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [data-received? @(r/subscribe [:item-viewer/data-received? :services.viewer])]
          [common/item-viewer-item-info :services.viewer {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-quantity-unit
  []
  (let [viewer-disabled?      @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])
        service-quantity-unit @(r/subscribe [:x.db/get-item [:services :viewer/viewed-item :quantity-unit :label]])]
       [common/data-element ::service-quantity-unit
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :quantity-unit
                             :placeholder "-"
                             :value       service-quantity-unit}]))

(defn- service-description
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])
        service-description @(r/subscribe [:x.db/get-item [:services :viewer/viewed-item :description]])]
       [common/data-element ::service-description
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :description
                             :placeholder "-"
                             :value       service-description}]))

(defn- service-item-number
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])
        service-item-number @(r/subscribe [:x.db/get-item [:services :viewer/viewed-item :item-number]])]
       [common/data-element ::service-item-number
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :item-number
                             :placeholder "-"
                             :value       service-item-number}]))

(defn- service-price
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])
        service-price    @(r/subscribe [:x.db/get-item [:services :viewer/viewed-item :unit-price]])]
       [common/data-element ::service-price
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :unit-price
                             :placeholder "-"
                             :value       {:content service-price :suffix " EUR"}}]))

(defn- service-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])
        service-name     @(r/subscribe [:x.db/get-item [:services :viewer/viewed-item :name]])]
       [common/data-element ::service-name
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :name
                             :placeholder "-"
                             :value       service-name}]))

(defn- service-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])]
       [common/surface-box ::service-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [service-name]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [service-quantity-unit]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [service-price]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [service-item-number]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [service-description]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-overview
  []
  [:<> [service-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :services.viewer])]
       (case current-view-id :overview [service-overview])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])]
       [common/item-viewer-menu-bar :services.viewer
                                    {:disabled?  viewer-disabled?
                                     :menu-items [{:label :overview}]}]))

(defn- controls
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])
        service-id       @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        edit-item-uri     (str "/@app-home/services/"service-id"/edit")]
       [common/item-viewer-controls :services.viewer
                                    {:disabled?      viewer-disabled?
                                     :edit-item-uri edit-item-uri}]))

(defn- breadcrumbs
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])
        service-name     @(r/subscribe [:x.db/get-item [:services :viewer/viewed-item :name]])]
       [common/surface-breadcrumbs :services.viewer/view
                                   {:crumbs [{:label :app-home    :route "/@app-home"}
                                             {:label :services    :route "/@app-home/services"}
                                             {:label service-name :placeholder :unnamed-service}]
                                    :disabled? viewer-disabled?}]))

(defn- label
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :services.viewer])
        service-name     @(r/subscribe [:x.db/get-item [:services :viewer/viewed-item :name]])]
       [common/surface-label :services.viewer/view
                             {:disabled?   viewer-disabled?
                              :label       service-name
                              :placeholder :unnamed-service}]))

(defn- header
  []
  [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "48px"}}
             [:div [label]
                   [breadcrumbs]]
             [:div [controls]]]
       [elements/horizontal-separator {:size :xxl}]
       [menu-bar]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  []
  [:<> [header]
       [body]
       [footer]])

(defn- service-viewer
  []
  [item-viewer/body :services.viewer
                    {:auto-title?   true
                     :error-element [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element #'common/item-viewer-ghost-element
                     :item-element  #'view-structure
                     :item-path     [:services :viewer/viewed-item]
                     :label-key     :name}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'service-viewer}])
