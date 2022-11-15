
(ns app.models.frontend.viewer.views
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

(defn- type-item-structure
  [lister-id item-dex {:keys [id modified-at name]}]
  (let [timestamp @(r/subscribe [:activities/get-actual-timestamp modified-at])]
       [common/list-item-structure lister-id item-dex
                                   {:cells [[:div {:style {:height "60px" :width "12px"}}]
                                            [:div {:style {:padding-left "6px"}}]
                                            [common/list-item-primary-cell lister-id item-dex {:label name :description timestamp :stretch? true :placeholder :unnamed-type}]
                                            [common/list-item-marker       lister-id item-dex {:icon :drag_handle :style {:cursor :grab}}]]}]))

(defn- type-item
  [lister-id item-dex {:keys [id] :as type-item}]
  (let [model-id @(r/subscribe [:x.router/get-current-route-path-param :item-id])]
       [elements/toggle {:content     [type-item-structure lister-id item-dex type-item]
                         :hover-color :highlight
                         :on-click    [:x.router/go-to! (str "/@app-home/models/"model-id"/types/"id)
                                                      {:route-parent (str "/@app-home/models/"model-id"/types")}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-lister-action-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :models.viewer])
        model-id         @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        on-click [:x.router/go-to! (str "/@app-home/models/"model-id"/types/create")]]
       [common/action-bar ::type-lister-action-bar
                          {:disabled? viewer-disabled?
                           :indent    {:bottom :xs}
                           :label     :add-type!
                           :on-click  on-click}]))

(defn- type-list
  [lister-id items]
  [common/item-list lister-id {:item-element #'type-item :items items}])

(defn- type-lister
  []
  (let [model-types @(r/subscribe [:db/get-item [:models :viewer/viewed-item :types]])]
       [item-lister/body :types.lister
                         {:default-order-by :modified-at/descending
                          :items-path    [:types :lister/downloaded-items]
                          :error-element [common/error-content {:error :the-content-you-opened-may-be-broken}]
                          :ghost-element #'common/item-lister-ghost-element
                          :list-element  #'type-list
                          :placeholder   :no-types-to-show
                          :prefilter     {:$or model-types}}]))

(defn- model-types-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :models.viewer])]
       [common/surface-box ::model-types-box
                           {:content [:<> [type-lister-action-bar]
                                          [type-lister]]
                            :disabled? viewer-disabled?
                            :label     :types
                            :overflow  :hidden}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-types
  []
  [:<> [model-types-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-thumbnail-preview
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :models.viewer])
        model-thumbnail  @(r/subscribe [:db/get-item [:models :viewer/viewed-item :thumbnail]])]
       [storage/media-preview ::model-thumbnail-preview
                              {:disabled?   viewer-disabled?
                               :indent      {:top :m :vertical :s}
                               :items       [model-thumbnail]
                               :placeholder "-"}]))

(defn- model-thumbnail-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :models.viewer])]
       [common/surface-box ::model-thumbnail-box
                           {:indent  {:top :m}
                            :content [:<> [model-thumbnail-preview]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-tags
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :models.viewer])]
       [elements/chip-group {:disabled?           viewer-disabled?
                             :horizontal-position :left
                             :indent              {:top :m :vertical :s}
                             :label               :tags
                             :value-path          [:models :viewer/viewed-item :tags]
                             :no-chips-label      :no-tags-selected}]))

(defn- model-description
  []
  (let [viewer-disabled?  @(r/subscribe [:item-viewer/viewer-disabled? :models.viewer])
        model-description @(r/subscribe [:db/get-item [:models :viewer/viewed-item :description]])]
       [common/data-element ::model-description
                            {:indent      {:top :m :vertical :s}
                             :label       :description
                             :placeholder "-"
                             :value       model-description}]))

(defn- model-product-description
  []
  (let [viewer-disabled?          @(r/subscribe [:item-viewer/viewer-disabled? :models.viewer])
        model-product-description @(r/subscribe [:db/get-item [:models :viewer/viewed-item :product-description]])]
       [common/data-element ::model-product-description
                            {:disabled? viewer-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :product-description
                             :value     model-product-description}]))

(defn- model-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :models.viewer])
        model-name       @(r/subscribe [:db/get-item [:models :viewer/viewed-item :name]])]
       [common/data-element ::model-name
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :name
                             :placeholder "-"
                             :value       model-name}]))

(defn- model-type-count
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :models.viewer])
        model-types      @(r/subscribe [:db/get-item [:models :viewer/viewed-item :types]])
        model-type-count  (count model-types)]
       [common/data-element ::model-type-count
                            {:disabled? viewer-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :type-count
                             :value     (str model-type-count)}]))

(defn- model-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :models.viewer])]
       [common/surface-box ::model-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [model-name]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [model-product-description]]
                                                [:div (forms/form-block-attributes {:ratio 34})
                                                      [model-type-count]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [model-tags]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [model-description]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-overview
  []
  [:<> [model-data-box]
       [model-thumbnail-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:gestures/get-current-view-id :models.viewer])]
       (case current-view-id :overview [model-overview]
                             :types    [model-types])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :models.viewer])]
       [common/item-viewer-menu-bar :models.viewer
                                    {:disabled?  viewer-disabled?
                                     :menu-items [{:label :overview}
                                                  {:label :types}]}]))

(defn- controls
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :models.viewer])
        model-id         @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        edit-item-uri     (str "/@app-home/models/"model-id"/edit")]
       [common/item-viewer-controls :models.viewer
                                    {:disabled?     viewer-disabled?
                                     :edit-item-uri edit-item-uri}]))

(defn- breadcrumbs
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :models.viewer])
        model-name       @(r/subscribe [:db/get-item [:models :viewer/viewed-item :name]])]
       [common/surface-breadcrumbs :models.viewer/view
                                   {:crumbs [{:label :app-home  :route "/@app-home"}
                                             {:label :models    :route "/@app-home/models"}
                                             {:label model-name :placeholder :unnamed-model}]
                                    :disabled? viewer-disabled?}]))

(defn- label
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :models.viewer])
        model-name       @(r/subscribe [:db/get-item [:models :viewer/viewed-item :name]])]
       [common/surface-label :models.viewer/view
                             {:disabled?   viewer-disabled?
                              :label       model-name
                              :placeholder :unnamed-model}]))

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
       [body]])

(defn- model-viewer
  []
  [item-viewer/body :models.viewer
                    {:auto-title?   true
                     :error-element [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element #'common/item-viewer-ghost-element
                     :item-element  #'view-structure
                     :item-path     [:models :viewer/viewed-item]
                     :label-key     :name}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'model-viewer}])
