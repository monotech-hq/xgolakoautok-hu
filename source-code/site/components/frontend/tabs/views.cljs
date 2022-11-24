
(ns site.components.frontend.tabs.views
  (:require
    [reagent.api :as reagent]
    [re-frame.api :as r]))

;; -----------------------------------------------------------------------------
;; ---- Utils ----

(defn vec->map [data]
  (apply hash-map data))

;; ---- Utils ----
;; -----------------------------------------------------------------------------

;; -----------------------------------------------------------------------------
;; ---- Components ----

(defn- menu-item [{:keys [view-id tab-id active?]}]
  [:button {:class       "tabs--button"
            :data-active active?
            :on-click    #(r/dispatch [:x.db/set-item! [view-id] tab-id])}
    tab-id])

(defn- menu-bar [current-tab-id view-id tabs]
  [:div {:class "tabs"}
    (map (fn [[tab-id _]]
           ^{:key tab-id}
           [menu-item {:view-id view-id
                       :tab-id  tab-id
                       :active? (= current-tab-id tab-id)}])
         tabs)])

(defn default-error-tab [& args]
  [:p (str "Something wrong! " args)])

(defn- tab-controller [current-tab-id tabs-data]
  (let [model (vec->map tabs-data)]
    (get model current-tab-id
      ; (if error-tab
        ; [error-tab]
        [default-error-tab current-tab-id tabs-data])))

(defn- tabs [{:keys [view-id tabs-color] :or {tabs-color ""}} key-value-pairs]
  (let [tabs-data      (partition 2 key-value-pairs)                            ;; Tabs key value pairs where key (keyword || string) and value ($)
        current-tab-id @(r/subscribe [:x.db/get-item [view-id]])]
    [:div.tabs--container {:style {"--tabs-color" tabs-color}}
     [menu-bar current-tab-id view-id tabs-data]
     [tab-controller current-tab-id key-value-pairs]]))

;; ---- Components ----
;; -----------------------------------------------------------------------------)

(defn did-mount [{:keys [view-id init-tab]} args]
  (r/dispatch [:x.db/set-item! [view-id] (if init-tab init-tab (first args))]))

(defn component [config & args]
  ;  @param {:config   {:view-id (keyword)
  ;                     :init-tab (keyword or string)(opt)}                     ;; if not included using the first value from args
  ;          :args     [(keyword or string), ($), ..]                           ;; args = key-value-pairs
  ;
  ; @usage [components/tabs {:view-id :my-view-id}                              ;; Tab
  ;          :tab-id [tab-1]                                                    ;; tab-is used also as title for menu items so
  ;          :tab-2  [tab-2]                                                    ;; it can be string or keyword for dictionary word
  ;          "tab-3" [tab-3]]
  ;
  ; @return [reagent-component]}
  (assert (even? (count args))
          (str "\n\n COMPONENTS/TABS FAILED!\n Args must be even!
               \n component config: \n"config"
               \n"))
  (reagent/lifecycles
    {:component-did-mount #(did-mount config args)
     :reagent-render
     (fn [config & args]
       [tabs config args])}))
