
(ns site.xgo.pages.price-quote.frontend.views
    (:require [re-frame.api                 :as r]
              [plugins.reagent.api                  :as reagent]
              [site.components.frontend.api :as components]
              [elements.api                 :as elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn title []
  [:h1#mt-price-quote--title "Árajánlat igénylés"])

(defn- breadcrumbs []
  (let [{:keys [category-name model-name type-name]} @(r/subscribe [:price-quote/breadcrumbs])
        {:keys [category model type]}                @(r/subscribe [:x.db/get-item [:filters]])]
    [elements/breadcrumbs {:crumbs [{:label       category-name
                                     :placeholder "category"
                                     :route       (str "/" category)}
                                    {:label       model-name
                                     :placeholder "model"
                                     :route       (str "/" category "/" model)}
                                    {:label       type-name
                                     :placeholder "type"
                                     :route       (str "/" category "/" model "/" type)}
                                    {:label       "Árajánlat"}]}]))

(defn- parts []
  [:div {:class "mt-price-quote--box"}
   "Parts"])

(defn- services []
 [:div {:class "mt-price-quote--box"} 
   "service"])

(defn- packages []
 [:div {:class "mt-price-quote--box"} "packages"])

(defn- step-1 []
  [:div {:id    "mt-price-quote--idk"}
         
   [components/tabs {:view-id :my-view-id}
     "Tartozékok"     [parts]
     "Szolgáltatások" [services]
     "Csomagok"       [packages]]])

(defn- name-field []
  [elements/text-field {:placeholder :name
                        :required?   true
                        :value-path  [:price-quote :name]}])

(defn- email-address-field []
  [elements/text-field {:placeholder :email-address
                        :required?   true
                        :indent {:top :m}
                        :value-path  [:price-quote :email]}])

(defn- phone-number-field []
  [elements/text-field {:placeholder :phone-number
                        :indent {:top :m}
                        :value-path  [:price-quote :phone-number]}])

(defn- company-name-field []
  [elements/text-field {:placeholder :company-name
                        :indent {:top :m}
                        :value-path  [:price-quote :company-name]}])

(defn- clients-data []
  [:div {:id    "mt-price-quote--client-data"
         :class "mt-price-quote--box"}
    [name-field]
    [email-address-field]
    [phone-number-field]
    [company-name-field]])

(defn overview []
 [:div 
  "Overview"])

(defn view
  []
  [:div#mt-price-quote
    [title]
    [breadcrumbs]
    [components/stepper {:finish-step-label "Igénylés"}
     "Tartozékok" {:content [step-1] :valid? true}
     "Adatok"     {:content [clients-data] :valid? (not (empty? @(r/subscribe [:step.one/valid?])))}
     "Áttekintés" {:content  [overview]
                   :valid?   true
                   :on-click (fn [] (.alert js/window "Congrats u finished!🐼"))}]])
  