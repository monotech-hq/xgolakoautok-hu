
(ns site.pages.main-page.backend.lifecycles
    (:require [x.server-core.api :as x.core]))

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-boot {:dispatch-n [[:environment/add-css! {:uri "/site/css/site.css"}]
                                 [:router/add-route! :main-page/route
                                                     {:client-event   [:main-page/load-page!]
                                                      :js-build       :site
                                                      :route-template "/"}]]}})
