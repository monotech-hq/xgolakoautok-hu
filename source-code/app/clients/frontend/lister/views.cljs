
(ns app.clients.frontend.lister.views
    (:require [app.common.frontend.api     :as common]
              [app.components.frontend.api :as components]
              [elements.api                :as elements]
              [keyword.api                 :as keyword]
              [layouts.surface-a.api       :as surface-a]
              [re-frame.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-lister-footer :clients.lister {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-list-item
  ; @param (keyword) lister-id
  ; @param (map) body-props
  ; @param (integer) item-dex
  ; @param (map) client-item
  [_ _ item-dex {:keys [colors first-name last-name email-address id modified-at phone-number]}]
  (let [timestamp   @(r/subscribe [:x.activities/get-actual-timestamp modified-at])
        client-name @(r/subscribe [:clients.lister/get-client-name item-dex])]
       [components/item-list-row {:cells [[components/list-item-avatar {:colors colors :first-name first-name :last-name last-name :size 42}]
                                          [components/list-item-cell   {:rows [{:content client-name :placeholder :unnamed-client}]}]
                                          [components/list-item-gap    {:width 12}]
                                          [components/list-item-cell   {:rows [{:content email-address :copyable? true :font-size :xs :color :muted}] :width 160}]
                                          [components/list-item-gap    {:width 12}]
                                          [components/list-item-cell   {:rows [{:content phone-number :copyable? true :font-size :xs :color :muted}] :width 160}]
                                          [components/list-item-gap    {:width 12}]
                                          [components/list-item-cell   {:rows [{:content timestamp :font-size :xs :color :muted}] :width 100}]
                                          [components/list-item-gap    {:width 12}]
                                          [components/list-item-button {:label :open! :width 100 :on-click [:x.router/go-to! (str "/@app-home/clients/"id)]}]
                                          [components/list-item-gap    {:width 12}]]
                                  :border (if (not= item-dex 0) :top)}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-list-header
  []
  (let [current-order-by @(r/subscribe [:item-lister/get-current-order-by :clients.lister])]
       [components/item-list-header ::client-list-header
                                    {:cells [{:width 78}
                                             {:label :name :order-by-key :name
                                              :on-click [:item-lister/order-items! :clients.lister :name]}
                                             {:width 12}
                                             {:label :email-address :width 160 :order-by-key :email-address
                                              :on-click [:item-lister/order-items! :clients.lister :email-address]}
                                             {:width 12}
                                             {:label :phone-number :width 160 :order-by-key :phone-number
                                              :on-click [:item-lister/order-items! :clients.lister :phone-number]}
                                             {:width 12}
                                             {:label :modified :width 100 :order-by-key :modified-at
                                              :on-click [:item-lister/order-items! :clients.lister :modified-at]}
                                             {:width 12}
                                             {:width 100}
                                             {:width 12}]
                                     :border :bottom
                                     :order-by current-order-by}]))

(defn- client-lister
  []
  [common/item-lister-body :clients.lister
                           {:list-item-element #'client-list-item
                            :item-list-header  #'client-list-header
                            :items-path        [:clients :lister/downloaded-items]}])

(defn- body
  []
  [components/surface-box ::body
                          {:content [:<> [client-lister]
                                         [elements/horizontal-separator {:height :xxs}]]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  [common/item-lister-header :clients.lister
                             {:crumbs    [{:label :app-home :route "/@app-home"}
                                          {:label :clients}]
                              :on-create [:x.router/go-to! "/@app-home/clients/create"]
                              :on-search [:item-lister/search-items! :clients.lister {:search-keys [:email-address :name :phone-number]}]
                              :search-placeholder :search-in-clients
                              :label              :clients}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
