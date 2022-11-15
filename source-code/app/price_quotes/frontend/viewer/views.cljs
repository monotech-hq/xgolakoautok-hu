
(ns app.price-quotes.frontend.viewer.views
    (:require [app.common.frontend.api                  :as common]
              [app.contents.frontend.api                :as contents]
              [app.clients.frontend.api                 :as clients]
              [app.packages.frontend.api                :as packages]
              [app.price-quote-templates.frontend.api   :as price-quote-templates]
              [app.price-quotes.frontend.viewer.helpers :as viewer.helpers]
              [app.products.frontend.api                :as products]
              [app.services.frontend.api                :as services]
              [app.vehicle-models.frontend.api          :as vehicle-models]
              [app.vehicle-types.frontend.api           :as vehicle-types]
              [elements.api                             :as elements]
              [engines.item-lister.api                  :as item-lister]
              [engines.item-viewer.api                  :as item-viewer]
              [forms.api                                :as forms]
              [mid-fruits.vector                        :as vector]
              [layouts.surface-a.api                    :as surface-a]
              [re-frame.api                             :as r]
              [time.api                                 :as time]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  (if-let [data-received? @(r/subscribe [:item-viewer/data-received? :price-quotes.viewer])]
          [common/item-viewer-item-info :price-quotes.viewer {}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-release-date
  []
  (let [viewer-disabled?         @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        price-quote-release-date @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :release-date]])]
       [common/data-element ::price-quote-release-date
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :release-date
                             :placeholder "-"
                             :value       (time/timestamp-string->date price-quote-release-date)}]))

(defn- price-quote-validity-interval
  []
  (let [viewer-disabled?              @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        price-quote-validity-interval @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :validity-interval]])]
       [common/data-element ::price-quote-validity-interval
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :validity-interval
                             :placeholder "-"
                             :value       {:content :n-days :replacements [price-quote-validity-interval]}}]))

(defn- price-quote-validity-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [common/surface-box ::price-quote-validity-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [price-quote-release-date]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [price-quote-validity-interval]]
                                                [:div (forms/form-block-attributes {:ratio 34})]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :label     :validity}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-more-items-price
  []
  (let [viewer-disabled?         @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        vehicle-more-items-price @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :more-items-price]])]
       [common/data-element ::price-quote-more-items-price
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :more-items-price
                             :placeholder "-"
                             :value       {:content vehicle-more-items-price :suffix " EUR"}}]))

(defn- price-quote-vehicle-count
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        vehicle-count    @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :vehicle-count]])]
       [common/data-element ::price-quote-vehicle-count
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :vehicle-count
                             :placeholder "-"
                             :value       vehicle-count}]))

(defn- price-quote-vehicle-unique-price
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        vehicle-unique-price @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :vehicle-unique-price]])]
       [common/data-element ::price-quote-vehicle-unique-price
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :vehicle-unique-price
                             :placeholder "-"
                             :value       {:content vehicle-unique-price :suffix " EUR"}}]))

(defn- price-quote-vehicle-unit-price
  []
  (let [viewer-disabled?   @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        vehicle-unit-price @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :vehicle-unit-price]])]
       [common/data-element ::price-quote-vehicle-unit-price
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :info-text   {:content :vehicle-unit-price-helper-n :replacements ["5"]}
                             :label       :vehicle-unit-price
                             :placeholder "-"
                             :value       {:content vehicle-unit-price :suffix " EUR"}}]))

(defn- price-quote-total-vat
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        total-vat         (viewer.helpers/get-total-vat)]
       [common/data-element ::price-quote-total-vat
                            {:disabled?   viewer-disabled?
                             :indent      {:top :s :vertical :s}
                             :label       :vat
                             :placeholder "-"
                             :value       {:content total-vat :suffix " EUR"}}]))

(defn- price-quote-net-total-price
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        net-total-price   (viewer.helpers/get-net-total-price)]
       [common/data-element ::price-quote-net-total-price
                            {:disabled?   viewer-disabled?
                             :indent      {:top :s :vertical :s}
                             :label       :net-total-price
                             :placeholder "-"
                             :value       {:content net-total-price :suffix " EUR"}}]))

(defn- price-quote-gross-total-price
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        gross-total-price (viewer.helpers/get-gross-total-price)]
       [common/data-element ::price-quote-gross-total-price
                            {:disabled?   viewer-disabled?
                             :indent      {:top :s :vertical :s}
                             :label       :gross-total-price
                             :placeholder "-"
                             :value       {:content gross-total-price :suffix " EUR"}}]))

(defn- price-quote-prices-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [common/surface-box ::price-quote-prices-box
                           {:content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [price-quote-vehicle-count]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [price-quote-vehicle-unit-price]]
                                                [:div (forms/form-block-attributes {:ratio 34})
                                                      (if-let [vehicle-unique-pricing? @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :vehicle-unique-pricing?]])]
                                                              [price-quote-vehicle-unique-price]
                                                              [price-quote-more-items-price])]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      (if-let [vehicle-unique-pricing? @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :vehicle-unique-pricing?]])]
                                                              [price-quote-more-items-price])]
                                                [:div (forms/form-block-attributes {:ratio 67})]]
                                          [elements/horizontal-line {:color :highlight :indent {:top :m}}]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [price-quote-net-total-price]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [price-quote-total-vat]]
                                                [:div (forms/form-block-attributes {:ratio 34})
                                                      [price-quote-gross-total-price]]
                                                [:div (forms/form-block-attributes {:ratio 34})]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :label     :prices}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-product-previews
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        price-quote-products @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :products]])]
       [products/product-preview ::price-quote-product-previews
                                 {:disabled?   viewer-disabled?
                                  :indent      {:top :m :vertical :s}
                                  :items       price-quote-products
                                  :placeholder "-"}]))

(defn- price-quote-products-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [common/surface-box ::price-quote-products-box
                           {:content [:<> [price-quote-product-previews]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :label     :products}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-service-previews
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        price-quote-services @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :services]])]
       [services/service-preview ::price-quote-service-previews
                                 {:indent      {:top :m :vertical :s}
                                  :items       price-quote-services
                                  :placeholder "-"}]))

(defn- price-quote-services-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [common/surface-box ::price-quote-services-box
                           {:content [:<> [price-quote-service-previews]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :label     :services}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-package-previews
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        price-quote-packages @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :packages]])]
       [packages/package-preview ::price-quote-package-previews
                                 {:indent      {:top :m :vertical :s}
                                  :items       price-quote-packages
                                  :placeholder "-"}]))

(defn- price-quote-packages-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [common/surface-box ::price-quote-packages-box
                           {:content [:<> [price-quote-package-previews]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :label     :packages}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-template-preview
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        template-link    @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :template]])]
       [price-quote-templates/template-preview ::price-quote-template-preview
                                               {:disabled?   viewer-disabled?
                                                :items       [template-link]
                                                :indent      {:top :m :vertical :s}
                                                :placeholder "-"}]))

(defn- price-quote-template-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [common/surface-box ::price-quote-template-box
                           {:content [:<> [price-quote-template-preview]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :template}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-client-preview
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        client-link      @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :client]])]
       [clients/client-preview ::price-quote-client-preview
                               {:disabled?   viewer-disabled?
                                :items       [client-link]
                                :indent      {:top :m :vertical :s}
                                :placeholder "-"}]))

(defn- price-quote-client-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [common/surface-box ::price-quote-client-box
                           {:content [:<> [price-quote-client-preview]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :label     :client}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-type-preview
  []
  (let [vehicle-count @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :vehicle-count]])
        type-link     @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :type]])]
       [vehicle-types/type-preview ::price-quote-type-preview
                                   {:item-count  vehicle-count
                                    :items       [type-link]
                                    :indent      {:top :m :vertical :s}
                                    :placeholder "-"}]))

(defn- price-quote-type-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [common/surface-box ::price-quote-type-box
                           {:content [:<> [price-quote-type-preview]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :icon      :view_list
                            :label     :vehicle-type}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-model-preview
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        model-link       @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :model]])]
       [vehicle-models/model-preview ::price-quote-model-preview
                                     {:disabled? viewer-disabled?
                                      :items       [model-link]
                                      :indent      {:top :m :vertical :s}
                                      :placeholder "-"}]))

(defn- price-quote-model-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [common/surface-box ::price-quote-model-box
                           {:content [:<> [price-quote-model-preview]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :indent    {:top :m}
                            :icon      :view_agenda
                            :label     :vehicle-model}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-overview
  []
  [:<> [price-quote-template-box]
       [price-quote-client-box]
       [price-quote-prices-box]
       [price-quote-model-box]
       [price-quote-type-box]
       [price-quote-products-box]
       [price-quote-services-box]
       [price-quote-packages-box]
       [price-quote-validity-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :price-quotes.viewer])]
       (case current-view-id :overview [price-quote-overview])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])]
       [common/item-viewer-menu-bar :price-quotes.viewer
                                    {:disabled?  viewer-disabled?
                                     :menu-items [{:label :overview}
                                                  {:label :preview :disabled? true}]}]))

(defn- controls
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        price-quote-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        edit-item-uri     (str "/@app-home/price-quotes/"price-quote-id"/edit")]
       [common/item-viewer-controls :price-quotes.viewer
                                    {:disabled?     viewer-disabled?
                                     :edit-item-uri edit-item-uri}]))

(defn- breadcrumbs
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        price-quote-name @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :name]])]
       [common/surface-breadcrumbs :price-quotes.viewer/view
                                   {:crumbs [{:label :app-home     :route "/@app-home"}
                                             {:label :price-quotes :route "/@app-home/price-quotes"}
                                             {:label price-quote-name}]
                                    :disabled? viewer-disabled?}]))

(defn- label
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :price-quotes.viewer])
        price-quote-name @(r/subscribe [:x.db/get-item [:price-quotes :viewer/viewed-item :name]])]
       [common/surface-label :price-quotes.viewer/view
                             {:disabled?   viewer-disabled?
                              :label       {:content :price-quote-n :replacements [price-quote-name]}}]))

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

(defn- price-quote-viewer
  []
  [item-viewer/body :price-quotes.viewer
                    {:auto-title?   true
                     :error-element [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element #'common/item-viewer-ghost-element
                     :item-element  #'view-structure
                     :item-path     [:price-quotes :viewer/viewed-item]
                     :label-key     :name}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'price-quote-viewer}])
