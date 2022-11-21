
(ns app.packages.frontend.viewer.views
    (:require [app.common.frontend.api              :as common]
              [app.components.frontend.api          :as components]
              [app.packages.frontend.viewer.queries :as viewer.queries]
              [app.storage.frontend.api             :as storage]
              [candy.api                            :refer [return]]
              [elements.api                         :as elements]
              [engines.item-lister.api              :as item-lister]
              [engines.item-viewer.api              :as item-viewer]
              [forms.api                            :as forms]
              [layouts.surface-a.api                :as surface-a]
              [re-frame.api                         :as r]
              [vector.api                           :as vector]
              [x.components.api                     :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [data-received? @(r/subscribe [:item-viewer/data-received? :packages.viewer])]
          [common/item-viewer-item-info :packages.viewer {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-item-structure
  [lister-id item-dex {:keys [id modified-at name quantity-unit]}]
  (let [timestamp     @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last?    @(r/subscribe [:item-lister/item-last? lister-id item-dex])
        service-count @(r/subscribe [:packages.viewer/get-service-count id])
        service-count  {:content (:value quantity-unit) :replacements [service-count]}]
       [common/list-item-structure {:cells [[    {:icon :workspace_premium}]
                                            [common/list-item-primary-cell {:label name :timestamp timestamp :stretch? true :description service-count :placeholder :unnamed-service}]
                                            [components/list-item-marker       {:icon :drag_handle :style {:cursor :grab}}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn- service-item
  [lister-id item-dex {:keys [id] :as service-item}]
  (let [package-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        service-route (str "/@app-home/services/"id)
        route-parent  (str "/@app-home/packages/"package-id"/services")]
       [elements/toggle {:content     [service-item-structure lister-id item-dex service-item]
                         :hover-color :highlight
                         :on-click    [:x.router/go-to! service-route {:route-parent route-parent}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-lister-action-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        on-click [:services.selector/load-selector! :packages.selector
                                                    {:autosave?     false
                                                     :multi-select? true
                                                     :on-change     [:packages.viewer/save-package-services!]
                                                     :value-path    [:packages :viewer/viewed-item :services]}]]
       [common/action-bar ::service-lister-action-bar
                          {:disabled? viewer-disabled?
                           :indent    {:bottom :xs}
                           :label     :select-services!
                           :on-click  on-click}]))

(defn- service-list
  [lister-id]
  (let [items @(r/subscribe [:item-lister/get-downloaded-items lister-id])]
       [common/item-list lister-id {:item-element #'service-item :items items}]))

(defn- service-lister
  []
  (let [package-services @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :services]])
        prefilter         {:$or (vector/->items package-services #(dissoc % :service/count))}]
       [item-lister/body :services.lister
                         {:default-order-by :modified-at/descending
                          :items-path    [:services :lister/downloaded-items]
                          :error-element [components/error-content {:error :the-content-you-opened-may-be-broken}]
                          :ghost-element #'common/item-lister-ghost-element
                          :list-element  #'service-list
                          :placeholder   :no-services-selected
                          :prefilter     prefilter

                          ; BUG#4048
                          ; Ha a package-services vektor elemeiben csak valamely kiválasztott elem(ek)
                          ; darabszáma változik meg, akkor a lista újratöltését triggerelni szükséges!
                          :trigger (random-uuid)}]))

(defn- package-services-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])]
       [common/surface-box ::package-services-box
                           {:content [:<> [service-lister-action-bar]
                                          [service-lister]]
                            :disabled? viewer-disabled?
                            :label     :services
                            :overflow  :hidden}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-services
  []
  [:<> [package-services-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-item-structure
  [lister-id item-dex {:keys [id modified-at name quantity-unit thumbnail]}]
  (let [timestamp     @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        item-last?    @(r/subscribe [:item-lister/item-last? lister-id item-dex])
        product-count @(r/subscribe [:packages.viewer/get-product-count id])
        product-count  {:content (:value quantity-unit) :replacements [product-count]}]
       [common/list-item-structure {:cells [[    {:thumbnail (:media/uri thumbnail)}]
                                            [common/list-item-primary-cell {:label name :timestamp timestamp :stretch? true :description product-count :placeholder :unnamed-product}]
                                            [components/list-item-marker       {:icon :drag_handle :style {:cursor :grab}}]]
                                    :separator (if-not item-last? :bottom)}]))

(defn- product-item
  [lister-id item-dex {:keys [id] :as product-item}]
  (let [package-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        product-route (str "/@app-home/products/"id)
        route-parent  (str "/@app-home/packages/"package-id"/products")]
       [elements/toggle {:content     [product-item-structure lister-id item-dex product-item]
                         :hover-color :highlight
                         :on-click    [:x.router/go-to! product-route {:route-parent route-parent}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- product-lister-action-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        on-click [:products.selector/load-selector! :packages.selector
                                                    {:autosave?     false
                                                     :multi-select? true
                                                     :on-change     [:packages.viewer/save-package-products!]
                                                     :value-path    [:packages :viewer/viewed-item :products]}]]
       [common/action-bar ::product-lister-action-bar
                          {:disabled? viewer-disabled?
                           :indent    {:bottom :xs}
                           :label     :select-products!
                           :on-click  on-click}]))

(defn- product-list
  [lister-id]
  (let [items @(r/subscribe [:item-lister/get-downloaded-items lister-id])]
       [common/item-list lister-id {:item-element #'product-item :items items}]))

(defn- product-lister
  []
  (let [package-products @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :products]])
        prefilter         {:$or (vector/->items package-products #(dissoc % :product/count))}]
       [item-lister/body :products.lister
                         {:default-order-by :modified-at/descending
                          :items-path    [:products :lister/downloaded-items]
                          :error-element [components/error-content {:error :the-content-you-opened-may-be-broken}]
                          :ghost-element #'common/item-lister-ghost-element
                          :list-element  #'product-list
                          :placeholder   :no-products-selected
                          :prefilter     prefilter

                          ; BUG#4048
                          ; Ha a package-products vektor elemeiben csak valamely kiválasztott elem(ek)
                          ; darabszáma változik meg, akkor a lista újratöltését triggerelni szükséges!
                          :trigger (random-uuid)}]))

(defn- package-products-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])]
       [common/surface-box ::package-products-box
                           {:content [:<> [product-lister-action-bar]
                                          [product-lister]]
                            :disabled? viewer-disabled?
                            :label     :products
                            :overflow  :hidden}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-products
  []
  [:<> [package-products-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-thumbnail-preview
  []
  (let [viewer-disabled?  @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-thumbnail @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :thumbnail]])]
       [storage/media-preview ::package-thumbnail-preview
                              {:disabled?   viewer-disabled?
                               :indent      {:top :m :vertical :s}
                               :items       [package-thumbnail]
                               :placeholder "-"
                               :thumbnail   {:height :3xl :width :5xl}}]))

(defn- package-thumbnail-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])]
       [common/surface-box ::package-thumbnail-box
                           {:indent  {:top :m}
                            :content [:<> [package-thumbnail-preview]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :thumbnail}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-quantity-unit
  []
  (let [viewer-disabled?      @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-quantity-unit @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :quantity-unit :label]])]
       [common/data-element ::package-quantity-unit
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :quantity-unit
                             :placeholder "-"
                             :value       package-quantity-unit}]))

(defn- package-description
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-description @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :description]])]
       [common/data-element ::package-description
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :description
                             :placeholder "-"
                             :value       package-description}]))

(defn- package-item-number
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-item-number @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :item-number]])]
       [common/data-element ::package-item-number
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :item-number
                             :placeholder "-"
                             :value       package-item-number}]))

(defn- package-pricing
  []
  (let [viewer-disabled?           @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-automatic-pricing? @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :automatic-pricing?]])]
       [common/data-element ::package-pricing
                            {:disabled? viewer-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :pricing
                             :value     (if package-automatic-pricing? :automatic-pricing :manual-pricing)}]))

(defn- package-automatic-price-querier
  []
  [x.components/querier ::package-automatic-price-querier
                        {:query (viewer.queries/request-automatic-price-query)}])

(defn- package-automatic-price
  []
  (let [viewer-disabled?        @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-automatic-price @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :automatic-price]])]
       [common/data-element ::package-automatic-price
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :unit-price
                             :placeholder "-"
                             :value       {:content package-automatic-price :suffix " EUR"}}]))

(defn- package-static-price
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-static-price @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :unit-price]])]
       [common/data-element ::package-static-price
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :unit-price
                             :placeholder "-"
                             :value       {:content package-static-price :suffix " EUR"}}]))

(defn- package-price
  []
  (if-let [automatic-pricing? @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :automatic-pricing?]])]
          [:<> [package-automatic-price-querier]
               [package-automatic-price]]
          [package-static-price]))

(defn- package-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-name     @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :name]])]
       [common/data-element ::package-name
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :name
                             :placeholder "-"
                             :value       package-name}]))

(defn- package-product-count
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-products     @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :products]])
        package-product-count (count package-products)]
       [common/data-element ::package-product-count
                            {:disabled? viewer-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :product-count
                             :value     (str package-product-count)}]))

(defn- package-service-count
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-services     @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :services]])
        package-service-count (count package-services)]
       [common/data-element ::package-service-count
                            {:disabled? viewer-disabled?
                             :indent    {:top :m :vertical :s}
                             :label     :service-count
                             :value     (str package-service-count)}]))

(defn- package-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])]
       [common/surface-box ::package-data-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [package-name]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [package-product-count]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [package-service-count]]
                                                [:div (forms/form-block-attributes {:ratio 34})
                                                      [package-pricing]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [package-quantity-unit]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [package-price]]
                                                [:div (forms/form-block-attributes {:ratio 34})
                                                      [package-item-number]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [package-description]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :label     :data
                            :disabled? viewer-disabled?}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-overview
  []
  [:<> [package-data-box]
       [package-thumbnail-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :packages.viewer])]
       (case current-view-id :overview [package-overview]
                             :products [package-products]
                             :services [package-services])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])]
       [common/item-viewer-menu-bar :packages.viewer
                                    {:disabled?  viewer-disabled?
                                     :menu-items [{:label :overview}
                                                  {:label :products}
                                                  {:label :services}]}]))

(defn- controls
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-id       @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        edit-item-uri     (str "/@app-home/packages/"package-id"/edit")]
       [common/item-viewer-controls :packages.viewer
                                    {:disabled?     viewer-disabled?
                                     :edit-item-uri edit-item-uri}]))

(defn- breadcrumbs
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-name     @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :name]])]
       [components/surface-breadcrumbs ::breadcrumbs
                                       {:crumbs [{:label :app-home    :route "/@app-home"}
                                                 {:label :packages    :route "/@app-home/packages"}
                                                 {:label package-name :placeholder :unnamed-package}]
                                        :disabled? viewer-disabled?}]))

(defn- label
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :packages.viewer])
        package-name     @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :name]])]
       [components/surface-label ::label
                                 {:disabled?   viewer-disabled?
                                  :label       package-name
                                  :placeholder :unnamed-package}]))

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

(defn- package-viewer
  []
  [item-viewer/body :packages.viewer
                    {:auto-title?   true
                     :error-element [components/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element #'common/item-viewer-ghost-element
                     :item-element  #'view-structure
                     :item-path     [:packages :viewer/viewed-item]
                     :label-key     :name}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'package-viewer}])
