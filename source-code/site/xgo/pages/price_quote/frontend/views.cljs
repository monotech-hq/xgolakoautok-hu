
(ns site.xgo.pages.price-quote.frontend.views
    (:require [re-frame.api                 :as r]
              [site.components.frontend.api :as components]
              [elements.api                 :as elements]
              [x.components.api          :as x.components]
              [app.products.frontend.api :as products]
              [app.packages.frontend.api :as packages]
              [lorem-ipsum.api :as lorem-ipsum]))

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

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- quantity-buttons [item-path]
 (let [qty @(r/subscribe [:price-quote.item.quantity/get item-path])]
   [:div {:class "mt-price-quote--list-item-quantity"}
     [elements/icon-button {:icon      :indeterminate_check_box
                            :color     :primary
                            :disabled? (zero? qty)
                            :on-click  (if (= 1 qty)
                                         [:price-quote.item/remove! item-path]
                                         [:price-quote.item.quantity/dec! item-path])}]
     [:p {:style {:position "absolute"}} qty]
     [elements/icon-button {:icon      :add_box
                            :color     :primary
                            :disabled? (= 9 qty)
                            :on-click  [:price-quote.item.quantity/inc! item-path]}]]))

(defn- lister [item data]
  [:div {:class "mt-price-quote--lister-container"}
    (doall (map item data))])

(defn- list-item-thumbnail [{:media/keys [uri]}]
  [elements/thumbnail {:uri           uri
                       :width         :xl
                       :height        :xl
                       :border-radius :s}])

(defn- list-item-name-description [name description]
 [:div.mt-price-quote--list-item-name-description-container
    [:p.mt-price-quote--list-item-name name]
    [:p.mt-price-quote--list-item-description description]])

;; ----------------------------------------------------------------------------
;; --- Parts ---

(defn- parts-item [[id {:keys [name description thumbnail unit-price quantity-unit] :as props}]]
  [:div {:key   id
         :class "mt-price-quote--list-item"}
   [list-item-thumbnail thumbnail]
   [list-item-name-description name description]
   [:p unit-price "Ft/" (x.components/content (:label quantity-unit))]
   [quantity-buttons [:products id]]])

(defn- parts []
  (let [products-data @(r/subscribe [:x.db/get-item [:site :products]])]
    [:div {:class "mt-price-quote--box"}
      [lister parts-item products-data]])) 

;; --- Parts ---
;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; --- Services ---

(defn- service-item [[id {:keys [name description thumbnail unit-price quantity-unit] :as props}]]
  [:div {:key   id
         :class "mt-price-quote--list-item"}
   [list-item-thumbnail thumbnail]
   [list-item-name-description name description]
   [:p unit-price "Ft/" (x.components/content (:label quantity-unit))]
   [quantity-buttons [:services id]]])

(defn- services []
  (let [services-data @(r/subscribe [:x.db/get-item [:site :services]])]
    [:div {:class "mt-price-quote--box"} 
      [lister service-item services-data]]))

;; --- Services ---
;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; --- Packages ---

(defn- package-item [[id {:keys [name description thumbnail unit-price automatic-price quantity-unit] :as props}]]
  [:div {:key   id
         :class "mt-price-quote--list-item"}
    [list-item-thumbnail thumbnail]
    [list-item-name-description name description]
    [:p automatic-price unit-price "Ft/" (x.components/content (:label quantity-unit))]
    [quantity-buttons [:packages id]]])

(defn- packages []
  (let [packages-data @(r/subscribe [:x.db/get-item [:site :packages]])]
    [:div {:class "mt-price-quote--box"}
      [lister package-item packages-data]]))

;; --- Packages ---
;; ----------------------------------------------------------------------------

(defn- accessories []
  [:div {:id "mt-price-quote--idk"}
    [components/tabs {:view-id :my-view-id}
      "Tartozékok"     [parts]
      "Szolgáltatások" [services]
      "Csomagok"       [packages]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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

;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; --- Overview ---

(defn- overview-item [[id qty]]
  [:div {:key id}
    [:p.mt-price-quote--list-item-name name]
    [:p qty]])

(defn- accessories-overview [{:keys [products services packages]}]
  [:div
    [:h3 "Tartozékok"]
    [:p (str @(r/subscribe [:price-quote.overview.accessories/get [:products products]]))]
    [:p (str @(r/subscribe [:price-quote.overview.accessories/get [:services services]]))]
    [:p (str @(r/subscribe [:price-quote.overview.accessories/get [:packages packages]]))]])
    

(defn- overview []
  (let [price-quote-data @(r/subscribe [:x.db/get-item [:price-quote]])]
    [:div {:class "mt-price-quote--box"}
      [:h2 "Áttekintés"]
      [accessories-overview price-quote-data]]))

;; --- Overviews ---
;; ----------------------------------------------------------------------------

(defn view
  []
  [:div#mt-price-quote
    [title]
    [breadcrumbs]
    [components/stepper {:finish-step-label "Igénylés"}
     "Tartozékok" {:content  [accessories]  :valid? true}
     "Adatok"     {:content  [clients-data] :valid? (not (empty? @(r/subscribe [:step.one/valid?])))}
     "Áttekintés" {:content  [overview]     :valid? true
                   :on-click (fn [] (.alert js/window "Congrats u finished!🐼"))}]])
  