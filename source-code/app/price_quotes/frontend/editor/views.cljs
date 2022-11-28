
(ns app.price-quotes.frontend.editor.views
    (:require [app.common.frontend.api                  :as common]
              [app.components.frontend.api              :as components]
              [app.price-quotes.frontend.editor.boxes   :as editor.boxes]
              [app.price-quotes.frontend.editor.helpers :as editor.helpers]
              [app.price-quotes.frontend.editor.queries :as editor.queries]
              [app.price-quotes.mid.handler.config      :as handler.config]
              [elements.api                             :as elements]
              [layouts.surface-a.api                    :as surface-a]
              [re-frame.api                             :as r]
              [x.components.api                         :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-editor-footer :price-quotes.editor {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- price-quote-preview
  []
  [:<> [editor.boxes/price-quote-preview-box]])

(defn- price-quote-template
  []
  [:<> [editor.boxes/price-quote-template-box]])

(defn- price-quote-prices
  []
  [:<> [editor.boxes/price-quote-prices-box]
       [editor.boxes/price-quote-unique-price-box]])

(defn- price-quote-items
  []
  [:<> [editor.boxes/price-quote-products-box]
       [editor.boxes/price-quote-services-box]
       [editor.boxes/price-quote-packages-box]])

(defn- price-quote-vehicle
  []
  (let [model-link @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :model]])
        type-link  @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :type]])]
       [:<> (if type-link  ^{:key ::price-quote-vehicle-box} [editor.boxes/price-quote-vehicle-box])
            (if :always    ^{:key ::price-quote-model-box}   [editor.boxes/price-quote-vehicle-model-box])
            (if model-link ^{:key ::price-quote-type-box}    [editor.boxes/price-quote-vehicle-type-box])]))

(defn- price-quote-client
  []
  [:<> [editor.boxes/price-quote-client-box]])

(defn- price-quote-settings
  []
  [:<> [editor.boxes/price-quote-validity-box]
       [editor.boxes/price-quote-more-settings-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :price-quotes.editor])]
       (case current-view-id :client   [price-quote-client]
                             :vehicle  [price-quote-vehicle]
                             :items    [price-quote-items]
                             :prices   [price-quote-prices]
                             :template [price-quote-template]
                             :settings [price-quote-settings]
                             :preview  [price-quote-preview])))

(defn- body
  ; @param (keyword) editor-id
  ; @param (map) server-response
  [_ _]
  [common/item-editor-body :price-quotes.editor
                           {:form-element     [view-selector]
                            :initial-item     {:release-date      (editor.helpers/initial-release-date) :version 1
                                               :validity-interval handler.config/DEFAULT-VALIDITY-INTERVAL}
                            :item-path        [:price-quotes :editor/edited-item]
                            :suggestions-path [:price-quotes :editor/suggestions]}])

(defn- preloader
  []
  [x.components/querier :price-quotes.editor/preloader
                        {:content     [body]
                         :placeholder [components/ghost-view {:layout :box-surface-body}]
                         :query       (editor.queries/request-server-date-query)}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [quote-name  @(r/subscribe [:x.db/get-item [:price-quotes :editor/edited-item :name]])
        quote-id    @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        quote-route @(r/subscribe [:item-editor/get-item-route :price-quotes.editor quote-id])]
       [common/item-editor-header :price-quotes.editor
                                  {:label       quote-name
                                   :placeholder :new-price-quote
                                   :crumbs      [{:label :app-home     :route "/@app-home"}
                                                 {:label :price-quotes :route "/@app-home/price-quotes"}
                                                 {:label quote-name    :route quote-route}]
                                   :menu-items  [{:label :template :change-keys [:template]}
                                                 {:label :client   :change-keys [:client]}
                                                 {:label :vehicle  :change-keys [:model :type]}
                                                 {:label :items    :change-keys [:packages :products :services]}
                                                 {:label :prices   :change-keys [:vehicle-unique-pricing? :vehicle-unique-price]}
                                                 {:label :settings :change-keys [:release-date :validity-interval :version]}
                                                 {:label :preview}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [preloader]
                                   [footer]]}])
