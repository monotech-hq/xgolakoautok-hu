
(ns site.xgo.pages.main-page.backend.lifecycles
    (:require [x.core.api :as x.core]))

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [
                                 [:x.router/add-route! :main-page/route
                                                     {:client-event   [:main-page/load-page!]
                                                      :js-build       :site
                                                      :route-template "/"}]
                                 [:x.router/add-route! :main-page.category/route
                                                     {:client-event   [:main-page/load-page!]
                                                      :js-build       :site
                                                      :route-template "/:category"}]
                                 [:x.router/add-route! :main-page.model/route
                                                   {:client-event   [:main-page/load-page!]
                                                    :js-build       :site
                                                    :route-template "/:category/:model"}]
                                 [:x.router/add-route! :main-page.type/route
                                                   {:client-event   [:main-page/load-page!]
                                                    :js-build       :site
                                                    :route-template "/:category/:model/:type"}]]}})
