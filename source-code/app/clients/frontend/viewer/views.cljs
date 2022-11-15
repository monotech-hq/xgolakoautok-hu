
(ns app.clients.frontend.viewer.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [engines.item-viewer.api :as item-viewer]
              [forms.api               :as forms]
              [layouts.surface-a.api   :as surface-a]
              [re-frame.api            :as r]
              [x.app-locales.api       :as x.locales]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-last-name
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-last-name @(r/subscribe [:db/get-item [:clients :viewer/viewed-item :last-name]])]
       [common/data-element ::client-last-name
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :last-name
                             :placeholder "-"
                             :value       client-last-name}]))

(defn- client-first-name
  []
  (let [viewer-disabled?  @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-first-name @(r/subscribe [:db/get-item [:clients :viewer/viewed-item :first-name]])]
       [common/data-element ::client-first-name
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :first-name
                             :placeholder "-"
                             :value       client-first-name}]))

(defn- client-name
  []
  [:div (forms/form-row-attributes)
        [x.locales/name-order [:div (forms/form-block-attributes {:ratio 50})
                                    [client-first-name]]
                              [:div (forms/form-block-attributes {:ratio 50})
                                    [client-last-name]]
                              @(r/subscribe [:locales/get-name-order])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-email-address
  []
  (let [viewer-disabled?     @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-email-address @(r/subscribe [:db/get-item [:clients :viewer/viewed-item :email-address]])]
       [common/data-element ::client-email-address
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :email-address
                             :placeholder "-"
                             :value       client-email-address}]))

(defn- client-phone-number
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-phone-number @(r/subscribe [:db/get-item [:clients :viewer/viewed-item :phone-number]])]
       [common/data-element ::client-phone-number
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :phone-number
                             :placeholder "-"
                             :value       client-phone-number}]))

(defn- client-primary-contacts
  []
  [:div (forms/form-row-attributes)
        [:div (forms/form-block-attributes {:ratio 50})
              [client-email-address]]
        [:div (forms/form-block-attributes {:ratio 50})
              [client-phone-number]]])

(defn- client-basic-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])]
       [common/surface-box ::client-basic-data-box
                           {:content [:<> [client-name]
                                          [client-primary-contacts]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :basic-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-company-name
  []
  (let [viewer-disabled?    @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-company-name @(r/subscribe [:db/get-item [:clients :viewer/viewed-item :company-name]])]
       [common/data-element ::client-company-name
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :company-name
                             :placeholder "-"
                             :value       client-company-name}]))

(defn- client-company-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])]
       [common/surface-box ::client-company-data-box
                           {:indent  {:top :m}
                            :content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [client-company-name]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :company-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-vat-no
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-vat-no    @(r/subscribe [:db/get-item [:clients :viewer/viewed-item :vat-no]])]
       [common/data-element ::client-vat-no
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :vat-no
                             :placeholder "-"
                             :value       client-vat-no}]))

(defn- client-country
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-country   @(r/subscribe [:db/get-item [:clients :viewer/viewed-item :country]])]
       [common/data-element ::client-country
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :country
                             :placeholder "-"
                             :value       client-country}]))

(defn- client-zip-code
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-zip-code  @(r/subscribe [:db/get-item [:clients :viewer/viewed-item :zip-code]])]
       [common/data-element ::client-zip-code
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :zip-code
                             :placeholder "-"
                             :value       client-zip-code}]))

(defn- client-city
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-city      @(r/subscribe [:db/get-item [:clients :viewer/viewed-item :city]])]
       [common/data-element ::client-city
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :city
                             :placeholder "-"
                             :value       client-city}]))

(defn- client-address
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-address   @(r/subscribe [:db/get-item [:clients :viewer/viewed-item :address]])]
       [common/data-element ::client-address
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :address
                             :placeholder "-"
                             :value       client-address}]))

(defn- client-billing-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])]
       [common/surface-box ::client-billing-data-box
                           {:indent  {:top :m}
                            :content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [client-country]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [client-zip-code]]
                                                [:div (forms/form-block-attributes {:ratio 34})
                                                      [client-city]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [client-address]]
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [client-vat-no]]
                                                [:div (forms/form-block-attributes {:ratio 34})]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :billing-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-tags
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])]
       [elements/chip-group {:disabled?           viewer-disabled?
                             :horizontal-position :left
                             :indent              {:top :m :vertical :s}
                             :label               :tags
                             :value-path          [:clients :viewer/viewed-item :tags]
                             :no-chips-label      "-"}]))

(defn- client-colors
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])]
       [common/item-viewer-color-stamp :clients.viewer
                                       {:disabled?  viewer-disabled?
                                        :indent     {:top :m :vertical :s}
                                        :label      :color
                                        :value-path [:clients :viewer/viewed-item :colors]}]))

(defn- client-description
  []
  (let [viewer-disabled?   @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-description @(r/subscribe [:db/get-item [:clients :viewer/viewed-item :description]])]
       [common/data-element ::client-description
                            {:disabled?   viewer-disabled?
                             :indent      {:top :m :vertical :s}
                             :label       :description
                             :placeholder "-"
                             :value       client-description}]))

(defn- client-more-data-box
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])]
       [common/surface-box ::client-more-data-box
                           {:indent  {:top :m}
                            :content [:<> [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 33})
                                                      [client-colors]]
                                                [:div (forms/form-block-attributes {:ratio 67})
                                                      [client-tags]]]
                                          [:div (forms/form-row-attributes)
                                                [:div (forms/form-block-attributes {:ratio 100})
                                                      [client-description]]]
                                          [elements/horizontal-separator {:size :s}]]
                            :disabled? viewer-disabled?
                            :label     :more-data}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-overview
  []
  [:<> [client-basic-data-box]
       [client-company-data-box]
       [client-billing-data-box]
       [client-more-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-price-quotes
  [])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:gestures/get-current-view-id :clients.viewer])]
       (case current-view-id :overview     [client-overview]
                             :price-quotes [client-price-quotes])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])]
       [common/item-viewer-menu-bar :clients.viewer
                                    {:disabled?  viewer-disabled?
                                     :menu-items [{:label :overview}
                                                  {:label :price-quotes}]}]))

(defn- controls
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-id        @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        edit-item-uri     (str "/@app-home/clients/"client-id"/edit")]
       [common/item-viewer-controls :clients.viewer
                                    {:disabled?     viewer-disabled?
                                     :edit-item-uri edit-item-uri}]))

(defn- breadcrumbs
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-name      @(r/subscribe [:clients.viewer/get-client-name])]
       [common/surface-breadcrumbs :clients.viewer/view
                                   {:crumbs [{:label :app-home   :route "/@app-home"}
                                             {:label :clients    :route "/@app-home/clients"}
                                             {:label client-name :placeholder :unnamed-client}]
                                    :disabled? viewer-disabled?}]))

(defn- label
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :clients.viewer])
        client-name      @(r/subscribe [:clients.viewer/get-client-name])]
       [common/surface-label :clients.viewer/view
                             {:disabled?   viewer-disabled?
                              :label       client-name
                              :placeholder :unnamed-client}]))

(defn- header
  []
  [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "48px"}}
             [:div [label]
                   [breadcrumbs]]
             (let [current-view-id @(r/subscribe [:gestures/get-current-view-id :clients.viewer])]
                  (if-not (= current-view-id :price-quotes)
                          [:div [controls]]))]
       [elements/horizontal-separator {:size :xxl}]
       [menu-bar]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  []
  [:<> [header]
       [body]])

(defn- client-viewer
  []
  [item-viewer/body :clients.viewer
                    {:auto-title?   true
                     :error-element [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element #'common/item-viewer-ghost-element
                     :item-element  #'view-structure
                     :item-path     [:clients :viewer/viewed-item]
                     :label-key     :name}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:content #'client-viewer}])
