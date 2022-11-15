
(ns app.vehicle-models.frontend.viewer.views
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
  (if-let [data-received? @(r/subscribe [:item-viewer/data-received? :vehicle-models.viewer])]
          [common/item-viewer-item-info :vehicle-models.viewer {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-item-structure
  [lister-id item-dex {:keys [id modified-at name]}]
  (let [timestamp  @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last? @(r/subscribe [:item-lister/item-last? lister-id item-dex])]
       [common/list-item-structure {:cells [[:div {:style {:height "60px" :width "12px"}}]
                                            [:div {:style {:padding-left "6px"}}]
                                            [common/list-item-primary-cell {:label name :description timestamp :stretch? true :placeholder :unnamed-vehicle-type}]
                                            [common/list-item-marker       {:icon :drag_handle :style {:cursor :grab}}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn- type-item
  [lister-id item-dex {:keys [id] :as type-item}]
  (let [model-id @(r/subscribe [:x.router/get-current-route-path-param :item-id])]
       [elements/toggle {:content     [type-item-structure lister-id item-dex type-item]
                         :hover-color :highlight
                         :on-click    [:x.router/go-to! (str "/@app-home/vehicle-models/"model-id"/types/"id)
                                                      {:route-parent (str "/@app-home/vehicle-models/"model-id"/types")}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- type-lister-action-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])
        model-id         @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        on-click [:x.router/go-to! (str "/@app-home/vehicle-models/"model-id"/types/create")]]
       [common/action-bar ::type-lister-action-bar
                          {:disabled? viewer-disabled?
                           :indent    {:bottom :xs}
                           :label     :add-vehicle-type!
                           :on-click  on-click}]))

(defn- type-list
  [lister-id items]
  [common/item-list lister-id {:item-element #'type-item :items items}])

(defn- type-lister
  []
  (let [model-types @(r/subscribe [:x.db/get-item [:vehicle-models :viewer/viewed-item :types]])]
       [item-lister/body :vehicle-types.lister
                         {:default-order-by :modified-at/descending
                          :items-path    [:vehicle-types :lister/downloaded-items]
                          :error-element [common/error-content {:error :the-content-you-opened-may-be-broken}]
                          :ghost-element #'common/item-lister-ghost-element
                          :list-element  #'type-list
                          :placeholder   :no-items-to-show
                          :prefilter     {:$or model-types}}]))

(defn- model-vehicle-types-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])]
       [common/surface-box ::model-vehicle-types-box
                           {:content [:<> [type-lister-action-bar]
                                          [type-lister]]
                            :disabled? viewer-disabled?
                            :label     :vehicle-types
                            :overflow  :hidden}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-vehicle-types
  []
  [:<> [model-vehicle-types-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- model-thumbnail-preview
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])
        model-thumbnail  @(r/subscribe [:x.db/get-item [:vehicle-models :viewer/viewed-item :thumbnail]])]
       [storage/media-preview ::model-thumbnail-preview
                              {:disabled?   viewer-disabled?
                               :indent      {:top :m :vertical :s}
                               :items       [model-thumbnail]
                               :placeholder "-"
                               :thumbnail   {:height :3xl :width :5xl}}]))

(defn- model-thumbnail-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])]
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
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])]
       [elements/chip-group {:disabled?           viewer-disabled?
                             :horizontal-position :left
                             :indent              {:top :m :vertical :s}
                             :label               :tags
                             :placeholder         "-"
                             :value-path          [:vehicle-models :viewer/viewed-item :tags]}]))

(defn- model-description
  []
  (let [viewer-disabled?  @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])
        model-description @(r/subscribe [:x.db/get-item [:vehicle-models :viewer/viewed-item :description]])]
       [common/data-element ::model-description
                            {:disabled? viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :description
                             :placeholder "-"
                             :value       model-description}]))

(defn- model-product-description
  []
  (let [viewer-disabled?          @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])
        model-product-description @(r/subscribe [:x.db/get-item [:vehicle-models :viewer/viewed-item :product-description]])]
       [common/data-element ::model-product-description
                            {:disabled? viewer-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :product-description
                             :value     model-product-description}]))

(defn- model-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])
        model-name       @(r/subscribe [:x.db/get-item [:vehicle-models :viewer/viewed-item :name]])]
       [common/data-element ::model-name
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :name
                             :placeholder "-"
                             :value       model-name}]))

(defn- model-type-count
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])
        model-types      @(r/subscribe [:x.db/get-item [:vehicle-models :viewer/viewed-item :types]])
        model-type-count  (count model-types)]
       [common/data-element ::model-type-count
                            {:disabled? viewer-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :vehicle-type-count
                             :value     (str model-type-count)}]))

(defn- model-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])]
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
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :vehicle-models.viewer])]
       (case current-view-id :overview      [model-overview]
                             :vehicle-types [model-vehicle-types]
                             ; Még létezik a ".../types" útvonal!
                             :types [model-vehicle-types])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])]
       [common/item-viewer-menu-bar :vehicle-models.viewer
                                    {:disabled?  viewer-disabled?
                                     :menu-items [{:label :overview}
                                                  {:label :vehicle-types}]}]))

(defn- controls
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])
        model-id         @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        edit-item-uri     (str "/@app-home/vehicle-models/"model-id"/edit")]
       [common/item-viewer-controls :vehicle-models.viewer
                                    {:disabled?     viewer-disabled?
                                     :edit-item-uri edit-item-uri}]))

(defn- breadcrumbs
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])
        model-name       @(r/subscribe [:x.db/get-item [:vehicle-models :viewer/viewed-item :name]])]
       [common/surface-breadcrumbs :vehicle-models.viewer/view
                                   {:crumbs [{:label :app-home       :route "/@app-home"}
                                             {:label :vehicle-models :route "/@app-home/vehicle-models"}
                                             {:label model-name :placeholder :unnamed-model}]
                                    :disabled? viewer-disabled?}]))

(defn- label
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :vehicle-models.viewer])
        model-name       @(r/subscribe [:x.db/get-item [:vehicle-models :viewer/viewed-item :name]])]
       [common/surface-label :vehicle-models.viewer/view
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
       [body]
       [footer]])

(defn- model-viewer
  []
  [item-viewer/body :vehicle-models.viewer
                    {:auto-title?   true
                     :error-element [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element #'common/item-viewer-ghost-element
                     :item-element  #'view-structure
                     :item-path     [:vehicle-models :viewer/viewed-item]
                     :label-key     :name}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'model-viewer}])
